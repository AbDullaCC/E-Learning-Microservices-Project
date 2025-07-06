package micro.course_service.services;

import lombok.RequiredArgsConstructor;
import micro.course_service.dtos.SessionDTO;
import micro.course_service.dtos.createDtos.CreateSessionDTO;
import micro.course_service.entities.Course;
import micro.course_service.entities.Session;
import micro.course_service.repositories.CourseRepository;
import micro.course_service.repositories.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing Session entities.
 * Handles business logic for Session entities without an explicit interface.
 */
@Service
@RequiredArgsConstructor // Generates a constructor with required arguments (final fields)
public class SessionService { // Renamed from SessionServiceImpl

    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;

    public SessionDTO createSession(CreateSessionDTO createSessionDTO) {

        Course course = courseRepository.findById(createSessionDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + createSessionDTO.getCourseId()));

        Session session = new Session();
        session.setCourse(course);
        session.setSession_order(createSessionDTO.getSessionOrder());
        session.setContent(createSessionDTO.getContent());


        // Save the session entity
        Session savedSession = sessionRepository.save(session);

        // Add the session to the course's list of sessions (maintaining bidirectional consistency)
        // This is important if you later fetch the Course and expect its sessions list to be up-to-date
        // without another database query.
        course.addSession(savedSession); // Assuming Course entity has an addSession helper method
        // You might need to save the course again if addSession modifies it and it's not cascaded
        // courseRepository.save(course); // Only needed if addSession doesn't trigger cascade or if course is detached

        // Convert saved Entity back to DTO for response
        return SessionDTO.getSessionDTO(
                savedSession.getId(),
                savedSession.getSession_order(),
                savedSession.getContent()
        );
    }

    public List<SessionDTO> getSessionsByCourseId(Long courseId) {
        // Find the course first to ensure it exists, or handle the case where it doesn't
        // If you don't care if the course exists and just want an empty list if it doesn't,
        // you can directly call sessionRepository.findByCourseId(courseId)
        courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        List<Session> sessions = sessionRepository.findByCourseId(courseId);

        // Convert list of Session entities to list of SessionDTOs
        return SessionDTO.listToDTO(sessions);
    }
}
