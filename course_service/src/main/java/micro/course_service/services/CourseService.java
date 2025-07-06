package micro.course_service.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import micro.course_service.dtos.CourseDTO;
import micro.course_service.dtos.CourseWithTeacherDTO;
import micro.course_service.dtos.UserDto;
import micro.course_service.dtos.createDtos.CreateCourseDTO;
import micro.course_service.entities.Course;
import micro.course_service.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CourseService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;

    @CircuitBreaker(name = "USER_SERVICE_CIRCUIT_BREAKER", fallbackMethod = "fallbackGetCourseById")
    public CourseWithTeacherDTO getCourseById(Long id) {
        Course course = courseRepository.getById(id);
        ResponseEntity<UserDto> response = restTemplate.getForEntity("http://user-service/userService/api/users/" + course.getUserId(), UserDto.class);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
            return null;
        return new CourseWithTeacherDTO(id, course.getName(), course.getPrice(), course.getDescription(), response.getBody());
    }



    public CourseWithTeacherDTO fallbackGetCourseById(Long id, Throwable throwable){
        Course course = courseRepository.getById(id);
        return new CourseWithTeacherDTO(id, course.getName(), course.getPrice(), course.getDescription(), null);
    }

    public CourseDTO createCourse(CreateCourseDTO createCourseDTO, Long teacherId) {
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
                savedCourse.getDescription()
        );
    }

}
