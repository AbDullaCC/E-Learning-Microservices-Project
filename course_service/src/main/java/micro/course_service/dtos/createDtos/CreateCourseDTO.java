package micro.course_service.dtos.createDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for creating a new Course.
 * Includes validation constraints for input fields.
 */
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class CreateCourseDTO {

    @NotBlank(message = "Course name cannot be empty")
    private String name;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be zero or a positive number")
    private BigDecimal price;

    private String description;
}

