package micro.exam_service.repository;

import micro.exam_service.entities.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findAllByExamId(Long examId);
}
