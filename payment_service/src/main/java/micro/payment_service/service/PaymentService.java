package micro.payment_service.service;

import lombok.RequiredArgsConstructor;
import micro.payment_service.dto.PaymentRequest;
import micro.payment_service.dto.PaymentResponse;
import micro.payment_service.model.Payment;
import micro.payment_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentResponse createPayment(PaymentRequest request) {
        Payment payment = Payment.builder()
                .userId(request.getUserId())
                .courseId(request.getCourseId())
                .price(request.getPrice())
                .date(LocalDateTime.now()) // default to now
                .build();

        Payment saved = paymentRepository.save(payment);

        return PaymentResponse.builder()
                .id(saved.getId())
                .userId(saved.getUserId())
                .courseId(saved.getCourseId())
                .price(saved.getPrice())
                .date(saved.getDate())
                .build();
    }

    public List<PaymentResponse> getPaymentsByUserId(Long userId) {

        List<Payment> payments = paymentRepository.findAllByUserId(userId);
        return payments.stream().map(payment ->
                PaymentResponse.builder()
                        .id(payment.getId())
                        .userId(payment.getUserId())
                        .courseId(payment.getCourseId())
                        .price(payment.getPrice())
                        .date(payment.getDate())
                        .build()
        ).collect(Collectors.toList());
    }
}
