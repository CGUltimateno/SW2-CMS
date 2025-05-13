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
    User student = userRepo.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Course course = courseRepo.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

    if (!enrollmentRepo.findByStudentAndCourse(student, course).isPresent()) {
        throw new RuntimeException("You must be enrolled in this course to submit feedback.");
    }

    FeedbackForm form = formRepo.findById(formId)
            .orElseThrow(() -> new RuntimeException("Feedback form not found"));

    FeedbackResponse response = FeedbackResponse.builder()
            .course(course)
            .form(form)
            .submittedAt(LocalDateTime.now())
            .build();

    FeedbackResponse savedResponse = responseRepo.save(response);

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

}

