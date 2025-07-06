package micro.course_service.dtos.createDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for creating a new Session.
 * Includes validation constraints for input fields.
 */
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class CreateSessionDTO {

    @NotNull(message = "Course ID cannot be null")
    @Min(value = 1, message = "Course ID must be a positive number")
    private Long courseId;

    @NotNull(message = "Session order cannot be null")
    @Min(value = 1, message = "Session order must be a positive number")
    private Integer sessionOrder;

    @NotBlank(message = "Session content cannot be empty")
    private String content;
}
