package com.coursefeedback.repository;
import java.util.List;
import com.coursefeedback.entity.Course;
import com.coursefeedback.entity.FeedbackForm;
import com.coursefeedback.entity.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackResponseRepository extends JpaRepository<FeedbackResponse, Long> {
      List<FeedbackResponse> findByCourse(Course course);
      List<FeedbackResponse> findByFormAndCourse(FeedbackForm form, Course course);

}
