package micro.course_service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object for the Course entity.
 * Used for exposing Course data via API, simplifying the entity structure.
 */
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class CourseWithTeacherDTO extends CourseDTO {
    private UserDto teacher; // The ID of the teacher

    public CourseWithTeacherDTO(Long id, String name, BigDecimal price, String description, List<SessionDTO> sessions, UserDto teacher) {
        super(id, name, price, description, sessions);
        this.teacher = teacher;
    }
}