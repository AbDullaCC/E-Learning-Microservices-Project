package micro.exam_service.dto;

public class StudentAnswerRequestDTO {
    private Long examId; // We keep examId to link to the exam within this service
    private String answers;

    // Getters and Setters

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }



    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }


}