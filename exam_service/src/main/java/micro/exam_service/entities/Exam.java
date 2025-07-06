package micro.exam_service.entities;

import jakarta.persistence.*;
import lombok.*; // Import Lombok annotations

@Entity
@Table(name = "exams")
// Lombok Annotations
@Getter                 // Generates all getter methods
@Setter                 // Generates all setter methods
@NoArgsConstructor      // Generates a no-argument constructor (required by JPA)
@AllArgsConstructor     // Generates a constructor with all arguments
@Builder                // The magic annotation that creates the Builder
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "questions", columnDefinition = "TEXT")
    private String questions;
}