package micro.course_service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import micro.course_service.entities.Session;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.stream.Collectors;

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

    public static SessionDTO getSessionDTO(Long id, Integer session_order, String content){
        return new SessionDTO(id, session_order, content);
    }

    public static List<SessionDTO> listToDTO(List<Session> sessions){
        return sessions.stream()
                .map(session -> getSessionDTO(
                        session.getId(),
                        session.getSession_order(),
                        session.getContent()
                ))
                .collect(Collectors.toList());
    }
}

