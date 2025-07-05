package micro.course_service.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a Session entity, which is part of a Course.
 * A session has content and an order within a course.
 */
@Entity
@Table(name = "sessions")
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetching for performance
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "session_order", nullable = false)
    private Integer session_order;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
}
