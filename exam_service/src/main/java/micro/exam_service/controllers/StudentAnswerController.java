package micro.exam_service.controllers;

import micro.exam_service.dto.EvaluationDTO;
import micro.exam_service.dto.StudentAnswerDTO;
import micro.exam_service.services.StudentAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answers")
public class StudentAnswerController {

    @Autowired
    private StudentAnswerService studentAnswerService;


    @PostMapping
    public ResponseEntity<StudentAnswerDTO> submitAnswer(@RequestBody StudentAnswerDTO answerDto) {
        StudentAnswerDTO savedAnswer = studentAnswerService.saveAnswer(answerDto);
        return new ResponseEntity<>(savedAnswer, HttpStatus.CREATED);
    }


    @PutMapping("/{id}/evaluation")
    public ResponseEntity<StudentAnswerDTO> updateEvaluation(
            @PathVariable("id") Long answerId,
            @RequestBody EvaluationDTO evaluationDto) {

        StudentAnswerDTO updatedAnswer = studentAnswerService.updateEvaluation(answerId, evaluationDto.getEvaluation());
        return new ResponseEntity<>(updatedAnswer, HttpStatus.OK);
    }
}