package micro.exam_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for the Course entity.
 * Used for exposing Course data via API, simplifying the entity structure.
 */
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class CourseWithTeacherDTO extends CourseDTO {
    private UserDTO teacher; // The ID of the teacher

    public CourseWithTeacherDTO(Long id, String name, BigDecimal price,String description, UserDTO teacher) {
        super(id, name, price, description);
        this.teacher = teacher;
    }
}