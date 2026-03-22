package github.hqn03.auth_service.payment.service;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Address;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import github.hqn03.auth_service.common.config.StripeProperties;
import github.hqn03.auth_service.common.exception.AppException;
import github.hqn03.auth_service.common.helper.JsonUtils;
import github.hqn03.auth_service.common.service.RedisService;
import github.hqn03.auth_service.order.dto.OrderItemDto;
import github.hqn03.auth_service.payment.dto.StripeData;
import github.hqn03.auth_service.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeService {
    private final StripeClient stripeClient;
    private final RedisService redisService;
    private final StripeProperties stripeProperties;
    private final JsonUtils jsonUtils;
    private final PaymentRepository paymentRepository;

    public String createCheckoutSession(String orderKey, String cartKey) throws StripeException {
        Map<Object, Object> orderItems = redisService.hGetAll(orderKey);
        if (orderItems.isEmpty()) {
            throw new RuntimeException("Đơn hàng đã hết hạn hoặc không tồn tại.");
        }

        List<SessionCreateParams.LineItem> stripeLineItems = orderItems.values().stream().map(
                jsonObj -> {
                    OrderItemDto orderItem = jsonUtils.deserialize(jsonObj.toString(), OrderItemDto.class);

                    return SessionCreateParams.LineItem.builder()
                            .setQuantity(orderItem.getQuantity().longValue())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("vnd")
                                            .setUnitAmountDecimal(orderItem.getPrice())
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(orderItem.getName())
                                                            .setDescription(orderItem.getVariant())
                                                            .addImage(orderItem.getImage())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build();
                }
        ).toList();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(stripeProperties.getSuccessUrl() + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(stripeProperties.getCancelUrl())
                .setPhoneNumberCollection(
                        SessionCreateParams.PhoneNumberCollection.builder().setEnabled(true).build()
                )
                .setShippingAddressCollection(
                        SessionCreateParams.ShippingAddressCollection.builder()
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.VN)
                                .build()
                )
                .putMetadata("orderKey", orderKey)
                .putMetadata("cartKey", cartKey)
                .addAllLineItem(stripeLineItems)
                .build();

        Session session = stripeClient.v1().checkout().sessions().create(params);
        return session.getUrl();
    }

    public StripeData getStripeData(String sessionId) throws StripeException {
        if (paymentRepository.existsBySessionId(sessionId)) {
            throw new AppException("Giao dich da hoan tat", HttpStatus.CONFLICT);
        }

        Session session = stripeClient.v1().checkout().sessions().retrieve(sessionId);
        if ("complete".equals(session.getStatus())) {
            String currency = session.getCurrency();
            String method = "UNKNOWN";
            if (session.getPaymentMethodTypes() != null && !session.getPaymentMethodTypes().isEmpty()) {
                method = session.getPaymentMethodTypes().getFirst(); // e.g., "card"
            }

            Session.CustomerDetails customerDetails = session.getCustomerDetails();
            String email = customerDetails.getEmail();
            Address address = customerDetails.getAddress();
            String addressString = address.getLine1() + " " + address.getLine2() + address.getCity() + " " + address.getState();
            String phone = customerDetails.getPhone();
            String name = customerDetails.getName();
            String orderKey = session.getMetadata().get("orderKey");
            String cartKey = session.getMetadata().get("cartKey");
            String username = email.substring(0, email.indexOf("@"));

            return new StripeData(email, addressString, phone, name, orderKey, username, currency, method, cartKey);
        }
        return null;
    }
}


//    public String createPaymentIntent(Long orderId, Long amount) throws StripeException {
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(amount)
//                .setCurrency("vnd")
//                .setAutomaticPaymentMethods(
//                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
//                                .setEnabled(true)
//                                .build()
//                )
//                .putMetadata("order_id", orderId.toString())
//                .build();
//
//        PaymentIntent intent = stripeClient.paymentIntents().create(params);
//        return intent.getClientSecret();
//    }

