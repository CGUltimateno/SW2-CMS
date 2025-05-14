package com.coursefeedback.controller;


import com.coursefeedback.service.FeedbackResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import com.coursefeedback.dto.FeedbackAnswerRequest;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackResponseController {

    private final FeedbackResponseService feedbackResponseService;


@PostMapping("/submit/{courseId}/{formId}")
@PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
public ResponseEntity<String> submitFeedback(
        @PathVariable Long courseId,
        @PathVariable Long formId,
        @RequestBody List<FeedbackAnswerRequest> answers,
        Principal principal) {

    System.out.println("🔍 Received feedback submission request:");
    System.out.println("👤 User: " + principal.getName());
    System.out.println("📝 Course ID: " + courseId);
    System.out.println("📝 Form ID: " + formId);
    System.out.println("📝 Answer count: " + (answers != null ? answers.size() : "null"));

    try {
        feedbackResponseService.submitFeedback(courseId, formId, answers, principal);
        return ResponseEntity.ok("✅ Feedback submitted successfully.");
    } catch (Exception e) {
        System.err.println("❌ Error processing feedback: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.badRequest().body("❌ Error: " + e.getMessage());
    }
}
}

