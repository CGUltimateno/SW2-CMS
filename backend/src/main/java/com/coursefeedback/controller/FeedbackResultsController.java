package com.coursefeedback.controller;

import com.coursefeedback.dto.FeedbackResultsDTO;
import com.coursefeedback.dto.QuestionResultDTO; 
import com.coursefeedback.service.FeedbackResultsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedback/results")
@RequiredArgsConstructor
public class FeedbackResultsController {

    private final FeedbackResultsService feedbackResultsService;
    
    @GetMapping("/{courseId}")
    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR')")
public ResponseEntity<FeedbackResultsDTO> getCourseResults(
        @PathVariable Long courseId,
        Principal principal) {
    
    try {
        FeedbackResultsDTO results = feedbackResultsService.getCourseResults(courseId, principal.getName());
        
        // Clean up the maps to prevent null key serialization errors
        if (results.getRatingQuestionResults() != null) {
            Map<String, QuestionResultDTO> cleanMap = new HashMap<>();
            results.getRatingQuestionResults().forEach((key, value) -> {
                if (key != null) {
                    cleanMap.put(key, value);
                }
            });
            results.setRatingQuestionResults(cleanMap);
        }
        
        if (results.getTextResponses() != null) {
            Map<String, List<String>> cleanMap = new HashMap<>();
            results.getTextResponses().forEach((key, value) -> {
                if (key != null) {
                    cleanMap.put(key, value);
                }
            });
            results.setTextResponses(cleanMap);
        }
        
        return ResponseEntity.ok(results);
    }
    catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
    }
}
    
    @GetMapping("/instructor")
    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR')")
    public ResponseEntity<List<FeedbackResultsDTO>> getAllInstructorResults(Principal principal) {
        List<FeedbackResultsDTO> results = feedbackResultsService.getAllResultsForInstructor(principal.getName());
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/export/{courseId}")
    @PreAuthorize("hasAuthority('ROLE_INSTRUCTOR')")
    public ResponseEntity<byte[]> exportFeedbackReport(@PathVariable Long courseId, Principal principal) {
        byte[] report = feedbackResultsService.generateFeedbackReport(courseId, principal.getName());
        
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=feedback-report.pdf")
            .header("Content-Type", "application/pdf")
            .body(report);
    }
}