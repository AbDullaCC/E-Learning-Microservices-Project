package micro.exam_service.controllers;

import jakarta.validation.Valid;
import micro.exam_service.dto.*;
import micro.exam_service.dto.EvaluationDTO;
import micro.exam_service.dto.SingleStudentAnswerDTO;
import micro.exam_service.dto.StudentAnswerDTO;
import micro.exam_service.dto.StudentAnswerRequestDTO;
import micro.exam_service.services.StudentAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class StudentAnswerController {
    @Autowired
    private StudentAnswerService studentAnswerService;

    @PostMapping
    public ResponseEntity<StudentAnswerDTO> submitAnswer(@RequestBody StudentAnswerRequestDTO answerDto , @RequestAttribute("userId") Long authenticatedUserId) {
        StudentAnswerDTO savedAnswer = studentAnswerService.saveAnswer(answerDto , authenticatedUserId);
        return new ResponseEntity<>(savedAnswer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleStudentAnswerDTO> getAnswerById(@PathVariable Long id ,  @RequestAttribute("role") String role ,  @RequestAttribute("userId") Long userId) {
        SingleStudentAnswerDTO answerDto = studentAnswerService.getAnswerById(id , role, userId);
        return ResponseEntity.ok(answerDto);
    }

    @PutMapping("/{id}/evaluation")
    public ResponseEntity<StudentAnswerDTO> updateEvaluation(
            @PathVariable("id") Long answerId,
            @RequestBody EvaluationDTO evaluationDto,
            @RequestAttribute("role") String role
            ) {

        StudentAnswerDTO updatedAnswer = studentAnswerService.updateEvaluation(answerId, evaluationDto.getEvaluation() , role);
        return new ResponseEntity<>(updatedAnswer, HttpStatus.OK);
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<StudentAnswerDTO>> getAnswersForExamById(@PathVariable("examId") Long examId, @RequestAttribute("role") String role) {
        List<StudentAnswerDTO> studentsAnswers = studentAnswerService.getStudentsAnswers(examId, role);
        return new ResponseEntity<>(studentsAnswers, HttpStatus.CREATED);
    }
}