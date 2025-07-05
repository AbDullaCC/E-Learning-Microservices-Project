package micro.course_service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for the Session entity.
 * Used for exposing Session data via API, simplifying the entity structure.
 */
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class SessionDTO {
    private Long id;
    private Integer session_order;
    private String content;
}
