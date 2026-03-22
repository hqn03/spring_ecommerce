package github.hqn03.auth_service.payment.service;

import github.hqn03.auth_service.order.repository.OrderRepository;
import github.hqn03.auth_service.payment.constant.PaymentStatus;
import github.hqn03.auth_service.payment.entity.Payment;
import github.hqn03.auth_service.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public void createPayment(String sessionId, PaymentStatus status, BigDecimal amount, String currency, Long orderId, String method) {
        Payment payment = Payment.builder()
                .sessionId(sessionId)
                .status(status)
                .amount(amount)
                .currency(currency)
                .order(orderRepository.getReferenceById(orderId))
                .paymentMethod(method)
                .build();

        paymentRepository.save(payment);
    }
}
