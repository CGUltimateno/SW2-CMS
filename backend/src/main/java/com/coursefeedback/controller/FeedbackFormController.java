package com.coursefeedback.controller;

import com.coursefeedback.dto.FeedbackFormDTO;
import com.coursefeedback.dto.FeedbackFormRequest;
import com.coursefeedback.entity.FeedbackForm;
import com.coursefeedback.entity.FeedbackQuestion;
import com.coursefeedback.dto.FeedbackQuestionRequest; 
import com.coursefeedback.mapper.FeedbackFormMapper;
import com.coursefeedback.service.FeedbackFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/feedback-forms")
@RequiredArgsConstructor
public class FeedbackFormController {

    private final FeedbackFormService feedbackFormService;

    //  Create a new feedback form
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<FeedbackFormDTO> createFeedbackForm(@RequestBody FeedbackFormRequest request) {
        // Debug logging
        System.out.println("=============== FORM CREATION DEBUG ===============");
        System.out.println("Form Title: " + request.getTitle());
        System.out.println("Form Description: " + request.getDescription());
        
        if (request.getQuestions() != null) {
            System.out.println("Questions count: " + request.getQuestions().size());
            for (int i = 0; i < request.getQuestions().size(); i++) {
                FeedbackQuestionRequest q = request.getQuestions().get(i);
                System.out.println("Question " + (i+1) + ":");
                System.out.println("  Text: '" + q.getQuestionText() + "'");
                System.out.println("  Is Rating: " + q.isRatingQuestion());
                System.out.println("  Target: " + q.getTarget());
            }
        } else {
            System.out.println("No questions in request!");
        }
        
        // Use the correctly-named service
        FeedbackForm form = feedbackFormService.createFeedbackForm(request);
        
        // Debug the created form
        System.out.println("Created form with ID: " + form.getId());
        if (form.getQuestions() != null) {
            System.out.println("Saved questions count: " + form.getQuestions().size());
            for (int i = 0; i < form.getQuestions().size(); i++) {
                FeedbackQuestion q = form.getQuestions().get(i);
                System.out.println("Saved Question " + (i+1) + ":");
                System.out.println("  ID: " + q.getId());
                System.out.println("  Text: '" + q.getQuestionText() + "'");
            }
        } else {
            System.out.println("No questions saved with form!");
        }
        System.out.println("=================================================");
        
        // Use static method call rather than injected mapper
        return ResponseEntity.ok(FeedbackFormMapper.toDTO(form));
    }

    //  Assign form to course
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{formId}/assign/{courseId}")
public ResponseEntity<FeedbackFormDTO> assignFormToCourse(
        @PathVariable Long formId,
        @PathVariable Long courseId) {
    FeedbackFormDTO formDTO = feedbackFormService.assignFormToCourse(formId, courseId);
    return ResponseEntity.ok(formDTO);
}

    // View all forms 
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<FeedbackFormDTO>> getAllForms() {
        return ResponseEntity.ok(feedbackFormService.getAllForms());
    }

    // Get forms assigned to your enrolled courses
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/my-forms")
     public ResponseEntity<List<FeedbackFormDTO>> getMyFeedbackForms(Principal principal) {
        List<FeedbackFormDTO> forms = feedbackFormService.getFormsForStudent(principal.getName());
        return ResponseEntity.ok(forms);
    }
}
