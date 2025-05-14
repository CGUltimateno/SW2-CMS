package com.coursefeedback.service;

import com.coursefeedback.dto.FeedbackFormDTO;
import com.coursefeedback.dto.FeedbackFormRequest;
import com.coursefeedback.dto.FeedbackQuestionRequest;
import com.coursefeedback.entity.*;
import com.coursefeedback.mapper.FeedbackFormMapper;
import com.coursefeedback.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 



@Service
@RequiredArgsConstructor
public class FeedbackFormService {

    private final FeedbackFormRepository formRepo;
    private final FeedbackQuestionRepository questionRepo;
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;
    private final EnrollmentRepository enrollmentRepo;

@Transactional
public FeedbackForm createFeedbackForm(FeedbackFormRequest request) {
    // Debug the entire request
    System.out.println("Request title: " + request.getTitle());
    System.out.println("Request description: " + request.getDescription());
    System.out.println("Questions size: " + (request.getQuestions() != null ? request.getQuestions().size() : "null"));
    
    FeedbackForm form = FeedbackForm.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .createdAt(LocalDateTime.now())
            .build();
    
    form = formRepo.save(form);
    
    if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
        List<FeedbackQuestion> questions = new ArrayList<>();
        
        for (FeedbackQuestionRequest questionRequest : request.getQuestions()) {
            // Enhanced debugging
            System.out.println("Processing question: " + questionRequest);
            System.out.println("Question text: " + questionRequest.getQuestionText());
            System.out.println("Is rating: " + questionRequest.isRatingQuestion());
            System.out.println("Target: " + questionRequest.getTarget());
            
            // Validate question text to ensure it's not null
            String questionText = questionRequest.getQuestionText();
            if (questionText == null || questionText.trim().isEmpty()) {
                // Provide a default if missing
                questionText = "Untitled Question";
                System.out.println("Setting default question text: " + questionText);
            }
            
            QuestionTarget target = QuestionTarget.GENERAL;
            if (questionRequest.getTarget() != null) {
                try {
                    target = QuestionTarget.valueOf(questionRequest.getTarget());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid target: " + questionRequest.getTarget());
                }
            }
            
boolean isRating = questionRequest.isRatingQuestion();
System.out.println("Is rating question? " + isRating + " (type: " + isRating + ")");

FeedbackQuestion question = FeedbackQuestion.builder()
        .questionText(questionText)
        .ratingQuestion(isRating) // Use the explicit boolean value
        .target(target)
        .form(form)
        .build();

// Log the built question
System.out.println("Built question - text: '" + question.getQuestionText() + 
                   "', isRating: " + question.isRatingQuestion());
            
            questions.add(question);
        }
        
        // Save all questions
        questionRepo.saveAll(questions);
        
        // Set questions on form for bidirectional relationship
        form.setQuestions(questions);
        
        // Update the form
        form = formRepo.save(form);
    }
    
    return form;
}
public FeedbackFormDTO assignFormToCourse(Long formId, Long courseId) {
    FeedbackForm form = formRepo.findById(formId)
            .orElseThrow(() -> new RuntimeException("Feedback Form not found"));

    Course course = courseRepo.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

    boolean alreadyAssigned = form.getAssignedCourses().stream()
            .anyMatch(c -> c.getId().equals(courseId));
            
    if (!alreadyAssigned) {
        form.getAssignedCourses().add(course);
        form = formRepo.save(form);
    }

    return FeedbackFormMapper.toDTO(form);
}

public List<FeedbackForm> getFormsAssignedToCourse(Long courseId) {
    Course course = courseRepo.findById(courseId)
        .orElseThrow(() -> new RuntimeException("Course not found"));
    
    return formRepo.findByAssignedCoursesContaining(course);
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
