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
@PreAuthorize("hasRole('USER')")
public ResponseEntity<String> submitFeedback(
        @PathVariable Long courseId,
        @PathVariable Long formId,
        @RequestBody List<FeedbackAnswerRequest> answers,
        Principal principal) {

    feedbackResponseService.submitFeedback(courseId, formId, answers, principal);
    return ResponseEntity.ok("✅ Feedback submitted successfully.");
}
}

