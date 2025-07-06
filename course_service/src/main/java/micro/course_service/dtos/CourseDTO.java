package micro.course_service.dtos;

import com.netflix.discovery.provider.Serializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Serializer
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class CourseDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private List<SessionDTO> sessions;
}