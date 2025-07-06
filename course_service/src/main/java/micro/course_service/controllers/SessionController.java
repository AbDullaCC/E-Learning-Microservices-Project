package micro.course_service.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import micro.course_service.dtos.SessionDTO;
import micro.course_service.dtos.createDtos.CreateSessionDTO;
import micro.course_service.services.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Session-related operations.
 */
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor // Generates a constructor with required arguments (final fields)
public class SessionController {

    private final SessionService sessionService;

    /**
     * Endpoint to create a new session for a course.
     * @param createSessionDTO The DTO containing session details for creation.
     * @return ResponseEntity with the created SessionDTO and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<SessionDTO> createSession(@Valid @RequestBody CreateSessionDTO createSessionDTO) {
        SessionDTO createdSession = sessionService.createSession(createSessionDTO);
        return new ResponseEntity<>(createdSession, HttpStatus.CREATED);
    }

    @GetMapping("/bycourse/{courseId}")
    public ResponseEntity<List<SessionDTO>> getSessionsByCourse(@PathVariable Long courseId) {
        List<SessionDTO> createdSession = sessionService.getSessionsByCourseId(courseId);
        return new ResponseEntity<>(createdSession, HttpStatus.CREATED);
    }

    // Other controller methods would go here (e.g., getSessionById, updateSession, deleteSession)
}
