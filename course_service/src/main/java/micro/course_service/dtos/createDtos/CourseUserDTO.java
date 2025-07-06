package micro.course_service.dtos.createDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for CourseUser entity.
 * Represents a user's enrollment in a course.
 */
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class CourseUserDTO {
    private Long id;
    private Long courseId;
    private Long userId;
}
