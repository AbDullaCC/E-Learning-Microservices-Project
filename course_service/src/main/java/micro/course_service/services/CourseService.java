package micro.course_service.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import micro.course_service.dtos.CourseDTO;
import micro.course_service.dtos.CourseWithTeacherDTO;
import micro.course_service.dtos.SessionDTO;
import micro.course_service.dtos.UserDto;
import micro.course_service.dtos.createDtos.CreateCourseDTO;
import micro.course_service.dtos.createDtos.PaymentRequest;
import micro.course_service.dtos.createDtos.PaymentResponse;
import micro.course_service.entities.Course;
import micro.course_service.entities.CourseUser;
import micro.course_service.repositories.CourseRepository;
import micro.course_service.repositories.CourseUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CourseService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseUserRepository courseUserRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);

    @CircuitBreaker(name = "USER_SERVICE_CIRCUIT_BREAKER", fallbackMethod = "fallbackGetCourseById")
    public CourseWithTeacherDTO getCourseById(Long id) {
        Course course = courseRepository.getById(id);
        ResponseEntity<UserDto> response = restTemplate.getForEntity("http://user-service/userService/api/users/" + course.getUserId(), UserDto.class);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
            return null;
        return new CourseWithTeacherDTO(id, course.getName(), course.getPrice(), course.getDescription(), SessionDTO.listToDTO(course.getSessions()), response.getBody());
    }



    public CourseWithTeacherDTO fallbackGetCourseById(Long id, Throwable throwable){
        Course course = courseRepository.getById(id);
        return new CourseWithTeacherDTO(id, course.getName(), course.getPrice(), course.getDescription(), SessionDTO.listToDTO(course.getSessions()), null);
    }

    public CourseDTO createCourse(CreateCourseDTO createCourseDTO, Long teacherId, String role) {
        if(!Objects.equals(role, "INSTRUCTOR")){
            throw new RuntimeException("You have to be Instructor to create a course" );
        }
        LOGGER.info("Attempting to create a course");

        // Convert DTO to Entity
        Course course = new Course(
                teacherId,
                createCourseDTO.getName(),
                createCourseDTO.getPrice(),
                createCourseDTO.getDescription()
        );

        // Save the course entity
        Course savedCourse = courseRepository.save(course);

        // Convert saved Entity back to DTO for response
        return new CourseDTO(
                savedCourse.getId(),
                savedCourse.getName(),
                savedCourse.getPrice(),
                savedCourse.getDescription(),
                SessionDTO.listToDTO(savedCourse.getSessions())
        );
    }

    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findByStatus("available");
        return courses.stream()
                .map(course -> new CourseDTO(
                        course.getId(),
                        course.getName(),
                        course.getPrice(),
                        course.getDescription(),
                        SessionDTO.listToDTO(course.getSessions())
                ))
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getPendingCourses() {
        List<Course> courses = courseRepository.findByStatus("pending");
        return courses.stream()
                .map(course -> new CourseDTO(
                        course.getId(),
                        course.getName(),
                        course.getPrice(),
                        course.getDescription(),
                        SessionDTO.listToDTO(course.getSessions())
                ))
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "PAYMENT_SERVICE_CIRCUIT_BREAKER", fallbackMethod = "fallbackEnrollInCourse")
    public ResponseEntity<PaymentResponse> enrollInCourse(Long courseId, Long userId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        if (courseUserRepository.findByCourseIdAndUserId(courseId, userId).isPresent()) {
            throw new RuntimeException("User with ID " + userId + " is already enrolled in course with ID " + courseId);
        }

        PaymentRequest paymentRequest = new PaymentRequest(userId, courseId, Double.parseDouble(course.getPrice().toString()));
        ResponseEntity<PaymentResponse> response = restTemplate.postForEntity("http://payment-service/paymentService/api/payments/create",paymentRequest, PaymentResponse.class);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
            return null;

        CourseUser courseUser = new CourseUser(course, userId);
        CourseUser savedCourseUser = courseUserRepository.save(courseUser);

        return ResponseEntity.ok(response.getBody());
    }

    public ResponseEntity<PaymentResponse> fallbackEnrollInCourse(Long courseId, Long userId, Throwable throwable){
        return ResponseEntity.status(400).body(PaymentResponse()) ;
    }

    public List<CourseDTO> getEnrolledCoursesByUserId(Long userId) {
        // Find all CourseUser enrollments for the given userId
        List<CourseUser> enrollments = courseUserRepository.findByUserId(userId);

        return enrollments.stream()
                .map(CourseUser::getCourse) // Get the Course object from each CourseUser
                .map(course -> new CourseDTO(
                        course.getId(),
                        course.getName(),
                        course.getPrice(),
                        course.getDescription(),
                        SessionDTO.listToDTO(course.getSessions())
                ))
                .collect(Collectors.toList());
    }
}
