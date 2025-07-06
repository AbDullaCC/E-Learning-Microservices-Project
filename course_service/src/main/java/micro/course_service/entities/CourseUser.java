package micro.course_service.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a CourseUser entity, linking a User to a Course they are enrolled in.
 * This acts as a many-to-many relationship table.
 */
@Entity
@Table(name = "course_users")
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class CourseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-One relationship with Course.
    // A CourseUser enrollment belongs to one Course.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // The ID of the user who is enrolling in the course.
    // This is a foreign key to the User service, so it's not a JPA-managed relationship.
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Custom constructor for convenience when creating new enrollments
    public CourseUser(Course course, Long userId) {
        this.course = course;
        this.userId = userId;
    }
}
