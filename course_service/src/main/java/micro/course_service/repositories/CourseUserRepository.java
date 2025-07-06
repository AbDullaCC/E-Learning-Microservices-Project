package micro.course_service.repositories;

// CourseUserRepository.java

import micro.course_service.entities.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CourseUser entities.
 * Extends JpaRepository to provide standard CRUD operations.
 */
@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {
    /**
     * Finds all CourseUser enrollments for a given user ID.
     * @param userId The ID of the user.
     * @return A list of CourseUser enrollments.
     */
    List<CourseUser> findByUserId(Long userId);

    /**
     * Finds all CourseUser enrollments for a given course ID.
     * @param courseId The ID of the course.
     * @return A list of CourseUser enrollments.
     */
    List<CourseUser> findByCourseId(Long courseId);

    /**
     * Finds a CourseUser enrollment by course ID and user ID.
     * Useful for checking if a user is already enrolled in a specific course.
     * @param courseId The ID of the course.
     * @param userId The ID of the user.
     * @return An Optional containing the CourseUser if found, or empty otherwise.
     */
    Optional<CourseUser> findByCourseIdAndUserId(Long courseId, Long userId);
}

