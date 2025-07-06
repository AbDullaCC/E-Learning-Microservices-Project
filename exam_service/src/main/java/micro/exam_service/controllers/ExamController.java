package micro.exam_service.controllers;

import micro.exam_service.dto.ExamDTO;
import micro.exam_service.services.ExamService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import micro.exam_service.dto.CreateExamRequestDTO; // <-- Add this import
import jakarta.validation.Valid; // <-- Add this import for validation


@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;
    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> getExamById(@PathVariable Long id) {
        ExamDTO examDto = examService.getExamDetails(id);
        if (examDto != null) {
            return new ResponseEntity<>(examDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ExamDTO> createExam(@Valid @RequestBody CreateExamRequestDTO requestDto) {
        ExamDTO createdExam = examService.createExam(requestDto);
        return new ResponseEntity<>(createdExam, HttpStatus.CREATED);
    }
}