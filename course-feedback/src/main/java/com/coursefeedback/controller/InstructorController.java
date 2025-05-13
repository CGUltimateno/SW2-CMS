package com.coursefeedback.controller;
import com.coursefeedback.dto.FeedbackSummaryDTO;
import com.coursefeedback.service.InstructorService;
import java.security.Principal;
import com.coursefeedback.dto.FeedbackSummaryDTO;
import com.coursefeedback.service.InstructorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@RestController
@RequestMapping("/instructor")
public class InstructorController {
    private final InstructorService instructorService;
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Welcome to the Instructor Dashboard 🎓");
    }
     @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/feedback-summary/{courseId}")
    public ResponseEntity<FeedbackSummaryDTO> getCourseFeedbackSummary(@PathVariable Long courseId, Principal principal) {
        FeedbackSummaryDTO summary = instructorService.getFeedbackSummary(courseId, principal.getName());
        return ResponseEntity.ok(summary);
}
}
