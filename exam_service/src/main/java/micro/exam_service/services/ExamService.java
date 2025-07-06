package micro.exam_service.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import micro.exam_service.dto.CourseWithTeacherDTO;
import micro.exam_service.dto.CreateExamRequestDTO;
import micro.exam_service.dto.ExamDTO;
import micro.exam_service.entities.Exam;
import micro.exam_service.repository.ExamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException; // <-- Add this import

import java.util.Optional;

@Service
public class ExamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamService.class);

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Define a constant for the circuit breaker name
    private static final String COURSE_SERVICE_BREAKER = "courseService";


    @CircuitBreaker(name = COURSE_SERVICE_BREAKER, fallbackMethod = "getExamDetailsFallback")
    public ExamDTO getExamDetails(Long id) {
        LOGGER.info("Attempting to get details for exam id: {}", id);
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));
        String courseServiceUrl = "http://course-service/courseService/api/courses/" + exam.getCourseId();
        ResponseEntity<CourseWithTeacherDTO> response = restTemplate.getForEntity(courseServiceUrl, CourseWithTeacherDTO.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch course details from course-service");
        }
        CourseWithTeacherDTO courseDto = response.getBody();

        return buildExamDTO(exam, courseDto);
    }


    @CircuitBreaker(name = COURSE_SERVICE_BREAKER, fallbackMethod = "createExamFallback")
    public ExamDTO createExam(CreateExamRequestDTO requestDto) {
        LOGGER.info("Attempting to create exam for course id: {}", requestDto.getCourseId());
        CourseWithTeacherDTO courseDto;
        try {
            String courseServiceUrl = "http://course-service/courseService/api/courses/" + requestDto.getCourseId();
            ResponseEntity<CourseWithTeacherDTO> response = restTemplate.getForEntity(courseServiceUrl, CourseWithTeacherDTO.class);
            courseDto = response.getBody();

            if (courseDto == null) {
                throw new RuntimeException("Course service returned an empty body for course ID: " + requestDto.getCourseId());
            }

        } catch (HttpClientErrorException.NotFound ex) {
            throw new RuntimeException(" Course not found with ID: " + requestDto.getCourseId());
        }
        Exam newExam = Exam.builder()
                .courseId(requestDto.getCourseId())
                .questions(requestDto.getQuestions())
                .build();
        Exam savedExam = examRepository.save(newExam);
        return buildExamDTO(savedExam, courseDto);
    }
    public ExamDTO getExamDetailsFallback(Long id, Throwable t) {
        LOGGER.warn("FALLBACK: Could not get details for exam id: {}. Reason: {}", id, t.getMessage());
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));
        return buildExamDTO(exam, getFallbackCourseDTO(exam.getCourseId()));
    }

    public ExamDTO createExamFallback(CreateExamRequestDTO requestDto, Throwable t) {
        throw new RuntimeException("Course service returned an empty body for course ID: " + requestDto.getCourseId());
    }


    private CourseWithTeacherDTO getFallbackCourseDTO(Long courseId) {
        return null;
    }


    private ExamDTO buildExamDTO(Exam exam, CourseWithTeacherDTO courseDto) {

        ExamDTO examDto = new ExamDTO();
        examDto.setId(exam.getId());
        examDto.setQuestions(exam.getQuestions());
        examDto.setCourse(courseDto);
        return examDto;
    }
}