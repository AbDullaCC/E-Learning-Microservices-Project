package micro.exam_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CreateExamRequestDTO {

    @NotNull(message = "Course ID cannot be null")
    private Long courseId;

    @NotEmpty(message = "Questions cannot be empty")
    private String questions;

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
}