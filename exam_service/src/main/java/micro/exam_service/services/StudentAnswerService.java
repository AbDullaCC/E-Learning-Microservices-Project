package micro.exam_service.services;

import micro.exam_service.dto.StudentAnswerDTO;
import micro.exam_service.dto.UserDTO;
import micro.exam_service.entities.StudentAnswer;
import micro.exam_service.repository.ExamRepository; // <-- 1. Import ExamRepository
import micro.exam_service.repository.StudentAnswerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StudentAnswerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentAnswerService.class);
    private final StudentAnswerRepository studentAnswerRepository;
    private final ExamRepository examRepository; // <-- Injected to check for exams

    public StudentAnswerService(StudentAnswerRepository studentAnswerRepository, ExamRepository examRepository) {
        this.studentAnswerRepository = studentAnswerRepository;
        this.examRepository = examRepository;
    }

    public StudentAnswerDTO saveAnswer(StudentAnswerDTO answerDto) {
        Long examId = answerDto.getExamId();
        LOGGER.info("Attempting to save answer for exam ID: {}", examId);
        if (!examRepository.existsById(examId)) {
            throw new RuntimeException("Exam not exist with id: " + examId);
        }
        StudentAnswer newAnswer = StudentAnswer.builder()
                .examId(examId)
                .userId(answerDto.getUserId())
                .answers(answerDto.getAnswers())
                .build();

        StudentAnswer savedAnswer = studentAnswerRepository.save(newAnswer);
        LOGGER.info("Successfully saved answer with ID: {} for exam ID: {}", savedAnswer.getId(), examId);
        return mapToDto(savedAnswer);
    }

    public StudentAnswerDTO updateEvaluation(Long answerId, String evaluation) {
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
}