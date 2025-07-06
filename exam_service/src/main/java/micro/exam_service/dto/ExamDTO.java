package micro.exam_service.dto;

public class ExamDTO {
    private Long id;
    private CourseWithTeacherDTO course;
    private String questions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseWithTeacherDTO getCourse() {
        return course;
    }

    public void setCourse(CourseWithTeacherDTO course) {
        this.course = course;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
}