package com.coursefeedback.controller;

import com.coursefeedback.dto.FeedbackFormDTO;
import com.coursefeedback.dto.FeedbackFormRequest;
import com.coursefeedback.entity.FeedbackForm;
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FeedbackFormDTO> createForm(@RequestBody FeedbackFormRequest request) {
        FeedbackForm created = feedbackFormService.createForm(request);
        return ResponseEntity.ok(FeedbackFormMapper.toDTO(created));
    }

    //  Assign form to course
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{formId}/assign/{courseId}")
    public ResponseEntity<FeedbackFormDTO> assignFormToCourse(
            @PathVariable Long formId,
            @PathVariable Long courseId) {
      FeedbackForm form = feedbackFormService.assignFormToCourse(formId, courseId);
    return ResponseEntity.ok(FeedbackFormMapper.toDTO(form));
    }

    // View all forms 
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<FeedbackFormDTO>> getAllForms() {
        return ResponseEntity.ok(feedbackFormService.getAllForms());
    }

    // Get forms assigned to your enrolled courses
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-forms")
     public ResponseEntity<List<FeedbackFormDTO>> getMyFeedbackForms(Principal principal) {
        List<FeedbackFormDTO> forms = feedbackFormService.getFormsForStudent(principal.getName());
        return ResponseEntity.ok(forms);
    }
}
