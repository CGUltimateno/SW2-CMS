package com.coursefeedback.service;
import com.coursefeedback.dto.FeedbackFormDTO;
import com.coursefeedback.dto.FeedbackFormRequest;
import com.coursefeedback.dto.FeedbackQuestionRequest;
import com.coursefeedback.entity.*;
import com.coursefeedback.mapper.FeedbackFormMapper;
import com.coursefeedback.repository.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;




@Service
@RequiredArgsConstructor
public class FeedbackFormService {

    private final FeedbackFormRepository formRepo;
    private final FeedbackQuestionRepository questionRepo;
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;
    private final EnrollmentRepository enrollmentRepo;
    public FeedbackForm createForm(FeedbackFormRequest request) {
        FeedbackForm form = FeedbackForm.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();

        // map questions
        List<FeedbackQuestion> questions = request.getQuestions().stream()
                .map(q -> FeedbackQuestion.builder()
                        .questionText(q.getQuestionText())
                        .isRatingQuestion(q.isRatingQuestion())
                        .target(q.getTarget())
                        .form(form)
                        .build())
                .toList();

        form.setQuestions(questions);

        return formRepo.save(form);  // Cascade saves questions too
    }

    public FeedbackForm assignFormToCourse(Long formId, Long courseId) {
        FeedbackForm form = formRepo.findById(formId)
                .orElseThrow(() -> new RuntimeException("Feedback Form not found"));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        form.getAssignedCourses().add(course);

        return formRepo.save(form);
    }

    public List<FeedbackFormDTO> getAllForms() {
    return formRepo.findAll()
            .stream()
            .map(FeedbackFormMapper::toDTO)
            .toList();
}
public List<FeedbackFormDTO> getFormsForStudent(String username) {
    User student = userRepo.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    List<Enrollment> enrollments = enrollmentRepo.findByStudent(student);
    Set<Long> enrolledCourseIds = enrollments.stream()
        .map(e -> e.getCourse().getId())
        .collect(Collectors.toSet());

    return formRepo.findAll().stream()
        .filter(form -> form.getAssignedCourses().stream()
            .anyMatch(c -> enrolledCourseIds.contains(c.getId())))
        .map(FeedbackFormMapper::toDTO)
        .toList();
}

    
}
