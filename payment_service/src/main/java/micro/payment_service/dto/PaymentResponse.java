package micro.payment_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long userId;
    private Long courseId;
    private Double price;
    private LocalDateTime date;
}
