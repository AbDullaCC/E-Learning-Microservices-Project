package micro.course_service.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import micro.course_service.entities.Session;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a Course entity in the Course Service.
 * A course is taught by a user (teacher) and can have multiple sessions.
 */
@Entity
@Table(name = "courses")
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
 @AllArgsConstructor // Generates a constructor with all fields.
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Session> sessions = new ArrayList<>();

    @Column(name = "status", nullable = false)
    private String status = "pending";

    // Custom constructor for creating a Course without an ID (which is generated)
    // and initializing the sessions list
    public Course(Long userId, String name, BigDecimal price, String description) {
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.sessions = new ArrayList<>(); // Initialize explicitly
    }

    /**
     * Helper method to add a session to the course.
     * It also sets the course on the session, maintaining bidirectional consistency.
     * @param session The session to add.
     */
    public void addSession(Session session) {
        sessions.add(session);
        session.setCourse(this);
    }

    /**
     * Helper method to remove a session from the course.
     * It also unsets the course on the session, maintaining bidirectional consistency.
     * @param session The session to remove.
     */
    public void removeSession(Session session) {
        sessions.remove(session);
        session.setCourse(null);
    }
}