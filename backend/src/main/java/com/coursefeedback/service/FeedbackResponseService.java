package com.coursefeedback.service;

import com.coursefeedback.dto.FeedbackAnswerRequest;
import com.coursefeedback.entity.*;
import com.coursefeedback.repository.*;
import lombok.RequiredArgsConstructor;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackResponseService {

    private final FeedbackFormRepository formRepo;
    private final CourseRepository courseRepo;
    private final FeedbackQuestionRepository questionRepo;
    private final FeedbackResponseRepository responseRepo;
    private final FeedbackAnswerRepository answerRepo;
    private final UserRepository userRepo;
    private final EnrollmentRepository enrollmentRepo;

    public void submitFeedback(Long courseId, Long formId, List<FeedbackAnswerRequest> answers, Principal principal) {
        // Get the authenticated student
        User student = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the course
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if student is enrolled
        if (!enrollmentRepo.findByStudentAndCourse(student, course).isPresent()) {
            throw new RuntimeException("You must be enrolled in this course to submit feedback.");
        }

        // Find the feedback form
        FeedbackForm form = formRepo.findById(formId)
                .orElseThrow(() -> new RuntimeException("Feedback form not found"));

        // Create the response
        FeedbackResponse response = FeedbackResponse.builder()
            .form(form)
            .course(course)
            .student(student)
            .submittedAt(LocalDateTime.now())
            .build();

        // Save the response first
        FeedbackResponse savedResponse = responseRepo.save(response);

        // Process and save answers
        List<FeedbackAnswer> answerEntities = answers.stream().map(answerDTO -> {
            FeedbackQuestion question = questionRepo.findById(answerDTO.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            return FeedbackAnswer.builder()
                    .question(question)
                    .rating(answerDTO.getRating())
                    .textAnswer(answerDTO.getTextAnswer())
                    .response(savedResponse)
                    .build();
        }).toList();

        answerRepo.saveAll(answerEntities);
    }

    public boolean hasStudentSubmittedFeedback(Long studentId, Long formId) {
        return responseRepo.existsByStudentIdAndFormId(studentId, formId);
    }
}