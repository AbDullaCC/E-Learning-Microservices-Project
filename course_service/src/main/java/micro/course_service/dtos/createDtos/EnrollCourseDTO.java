package micro.course_service.dtos.createDtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
/**
 * Data Transfer Object for enrolling a user in a course.
 * Includes validation constraints for input fields.
 */
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class EnrollCourseDTO {

    @NotNull(message = "Course ID cannot be null")
    @Min(value = 1, message = "Course ID must be a positive number")
    private Long courseId;

    @NotNull(message = "User ID cannot be null")
    @Min(value = 1, message = "User ID must be a positive number")
    private Long userId;
}
