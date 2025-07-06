package micro.exam_service.entities;

import jakarta.persistence.*;
import lombok.*; // Import all necessary Lombok annotations

@Entity
@Table(name = "student_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_id", nullable = false)
    private Long examId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "answers", nullable = false, columnDefinition = "TEXT")
    private String answers;

    @Column(name = "evaluation", nullable = true)
    private String evaluation;
}