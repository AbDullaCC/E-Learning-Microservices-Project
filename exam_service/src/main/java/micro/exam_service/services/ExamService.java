package micro.exam_service.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import micro.exam_service.dto.CourseDTO;
import micro.exam_service.dto.ExamDTO;
import micro.exam_service.entities.Exam;
import micro.exam_service.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private RestTemplate restTemplate;


    @HystrixCommand(fallbackMethod = "getExamDetailsFallback")
    public ExamDTO getExamDetails(Long id) {
        Optional<Exam> examOptional = examRepository.findById(id);
        if (!examOptional.isPresent()) {
            return null;
        }
        Exam exam = examOptional.get();

        String courseServiceUrl = "http://course-service/api/courses/" + exam.getCourseId();
        ResponseEntity<CourseDTO> response = restTemplate.getForEntity(courseServiceUrl, CourseDTO.class);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
            return null;
        CourseDTO courseDto = response.getBody();

        ExamDTO examDto = new ExamDTO();
        examDto.setId(exam.getId());
        examDto.setQuestions(exam.getQuestions());
        examDto.setCourse(courseDto);

        return examDto;
    }


    public ExamDTO x    (Long id) {
        Optional<Exam> examOptional = examRepository.findById(id);
        if (!examOptional.isPresent()) {
            return null;
        }
        Exam exam = examOptional.get();

        CourseDTO fallbackCourse = new CourseDTO();
        fallbackCourse.setId(exam.getCourseId());
        fallbackCourse.setTitle("Course information is currently unavailable");

        ExamDTO examDto = new ExamDTO();
        examDto.setId(exam.getId());
        examDto.setQuestions(exam.getQuestions());
        examDto.setCourse(fallbackCourse);

        return examDto;
    }
}