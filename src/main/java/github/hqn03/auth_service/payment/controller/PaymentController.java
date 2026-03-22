package github.hqn03.auth_service.payment.controller;

import com.stripe.exception.StripeException;
import github.hqn03.auth_service.order.service.OrderService;
import github.hqn03.auth_service.payment.dto.StripeData;
import github.hqn03.auth_service.payment.service.StripeService;
import github.hqn03.auth_service.user.entity.User;
import github.hqn03.auth_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final StripeService stripeService;
    private final OrderService orderService;
    private final UserService userService;

//    @PostMapping("/checkout-session")
//    public ResponseEntity<String> createCheckoutSession(@RequestBody CheckoutRequest request, @RequestHeader(value = "X-Session-ID", required = false) String sessionId) throws StripeException {
//        String url = stripeService.createCheckoutSession(request.skuIds(), sessionId);
//        return ResponseEntity.ok(url);
//    }

    @GetMapping("/success")
    public ResponseEntity<String>  handlePaymentSuccess(@RequestParam("session_id") String sessionId) throws StripeException {
        StripeData data = stripeService.getStripeData(sessionId);
        User user = userService.getOrCreateGuestUser(data.email(), data.username(), data.fullName(), data.address(), data.phone());
        orderService.createOrderAfterPayment(sessionId, data.orderKey(), data.cartKey(),user.getId(), data.fullName(), data.address(), data.phone(), data.currency(), data.method());

        return ResponseEntity.ok("Payment Success");
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> handlePaymentFail(){
        return ResponseEntity.ok("Error");
    }

    // 2. Dùng cho Payment Intents (Trả về clientSecret cho FE)
//    @PostMapping("/payment-intent")
//    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody OrderRequest request) {
//        String clientSecret = stripeService.createPaymentIntent(request);
//        return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
//    }
}
