package micro.exam_service.services;

import micro.exam_service.dto.StudentAnswerDTO;
import micro.exam_service.dto.StudentAnswerRequestDTO;
import micro.exam_service.dto.UserDTO;
import micro.exam_service.entities.StudentAnswer;
import micro.exam_service.repository.ExamRepository; // <-- 1. Import ExamRepository
import micro.exam_service.repository.StudentAnswerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import micro.exam_service.dto.SingleStudentAnswerDTO;
import java.util.Objects;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentAnswerService.class);
    private final StudentAnswerRepository studentAnswerRepository;
    private final ExamRepository examRepository; // <-- Injected to check for exams

    private final RestTemplate restTemplate;

    public StudentAnswerService(StudentAnswerRepository studentAnswerRepository, ExamRepository examRepository, RestTemplate restTemplate) {
        this.studentAnswerRepository = studentAnswerRepository;
        this.examRepository = examRepository;
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "getAnswerByIdFallback")
    public SingleStudentAnswerDTO getAnswerById(Long answerId , String role, Long id) {
        LOGGER.info("Attempting to get details for answer ID: {}", answerId);

        StudentAnswer answer = studentAnswerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found with ID: " + answerId));
        if(!Objects.equals(role, "INSTRUCTOR") && !Objects.equals(id, answer.getUserId())){
            System.out.println("we are here");
            throw new RuntimeException("You don't have permission to see this answer");
        }
        String userServiceUrl = "http://user-service/userService/api/users/" + answer.getUserId();
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(userServiceUrl, UserDTO.class);
        UserDTO userDto = response.getBody();
        if(!Objects.equals(userDto.getId(), answer.getUserId())){
            System.out.println("we are here");
            throw new RuntimeException("You don't have permission to see this answer");
        }
        return mapToSingleDto(answer, userDto);
    }


    public SingleStudentAnswerDTO getAnswerByIdFallback(Long answerId, Throwable t) {
        LOGGER.warn("FALLBACK: Could not get user details for answer ID: {}. Reason: {}", answerId, t.getMessage());

        StudentAnswer answer = studentAnswerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found with ID: " + answerId));
        return mapToSingleDto(answer, null);
    }


    // --- Helper mapping method ---
    private SingleStudentAnswerDTO mapToSingleDto(StudentAnswer entity, UserDTO userDto) {
        SingleStudentAnswerDTO dto = new SingleStudentAnswerDTO();
        dto.setId(entity.getId());
        dto.setExamId(entity.getExamId());
        dto.setAnswers(entity.getAnswers());
        dto.setEvaluation(entity.getEvaluation());
        dto.setUser(userDto); // This will be the fetched user or NULL from the fallback

        return dto;
    }


    public StudentAnswerDTO saveAnswer(StudentAnswerRequestDTO answerDto, Long authenticatedUserId) {
        Long examId = answerDto.getExamId();
        LOGGER.info("Attempting to save answer for exam ID: {}", examId);
        if (!examRepository.existsById(examId)) {
            throw new RuntimeException("Exam not exist with id: " + examId);
        }
        StudentAnswer newAnswer = StudentAnswer.builder()
                .examId(examId)
                .userId(authenticatedUserId)
                .answers(answerDto.getAnswers())
                .build();

        StudentAnswer savedAnswer = studentAnswerRepository.save(newAnswer);
        LOGGER.info("Successfully saved answer with ID: {} for exam ID: {}", savedAnswer.getId(), examId);
        return mapToDto(savedAnswer);
    }

    public StudentAnswerDTO updateEvaluation(Long answerId, String evaluation , String role) {
        System.out.println("the current role is "+role);
        if(!Objects.equals(role, "INSTRUCTOR")){
            System.out.println("we are here");
           throw new RuntimeException("You have to be Instructor to update evaluation " );
        }
        LOGGER.info("Attempting to update evaluation for answer ID: {}", answerId);

        StudentAnswer answer = studentAnswerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not exist with id: " + answerId));

        answer.setEvaluation(evaluation);
        StudentAnswer updatedAnswer = studentAnswerRepository.save(answer);
        LOGGER.info("Successfully updated evaluation for answer ID: {}", updatedAnswer.getId());

        return mapToDto(updatedAnswer);
    }

    private StudentAnswerDTO mapToDto(StudentAnswer entity) {
        // This mapping logic remains simple and effective
        UserDTO userDto = new UserDTO();
        userDto.setId(entity.getUserId());

        StudentAnswerDTO dto = new StudentAnswerDTO();
        dto.setId(entity.getId());
        dto.setExamId(entity.getExamId());
        dto.setAnswers(entity.getAnswers());
        dto.setEvaluation(entity.getEvaluation());
        dto.setUserId(userDto.getId());

        return dto;
    }

    public List<StudentAnswerDTO> getStudentsAnswers(Long examId, String role){
        if(!Objects.equals(role, "INSTRUCTOR")){
            throw new RuntimeException("You have to be Instructor to view the answers" );
        }
        LOGGER.info("Attempting to view the answers: {}", examId);

        List<StudentAnswer> studentAnswers= studentAnswerRepository.findAllByExamId(examId);
        List<StudentAnswerDTO> studentAnswerDTOS= new ArrayList<>();
        for (StudentAnswer ans : studentAnswers) {
            studentAnswerDTOS.add(mapToDto(ans));
        }
        return studentAnswerDTOS;
    }
}