package com.coursefeedback.controller;

import com.coursefeedback.dto.AverageRatingDTO;
import com.coursefeedback.dto.FeedbackSummaryDTO;
import com.coursefeedback.repository.FeedbackFormRepository;
import com.coursefeedback.repository.UserRepository;
import com.coursefeedback.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final UserRepository userRepo;
    private final FeedbackFormRepository formRepo;
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<FeedbackSummaryDTO> getRatingsForCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(ratingService.getCourseRatings(courseId));
    }

    
    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<FeedbackSummaryDTO> getRatingsForInstructor(@PathVariable Long instructorId) {
        return ResponseEntity.ok(ratingService.getInstructorRatings(instructorId));
    }
    @GetMapping("/instructor/{id}/average")
public ResponseEntity<AverageRatingDTO> getInstructorRating(@PathVariable Long id) {
    var instructor = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Instructor not found"));
    double avg = ratingService.calculateInstructorRating(id);
    return ResponseEntity.ok(new AverageRatingDTO(instructor.getFirstName() + " " + instructor.getLastName(), avg));
}
@GetMapping("/feedback-summary/form/{formId}/course/{courseId}")
@PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
public ResponseEntity<FeedbackSummaryDTO> getCombinedFeedbackSummary(
        @PathVariable Long formId,
        @PathVariable Long courseId) {
    return ResponseEntity.ok(ratingService.getFullFeedbackSummary(formId, courseId));
}

@GetMapping("/course/{courseId}/export-excel")
@PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
public ResponseEntity<byte[]> exportCourseRatingsExcel(@PathVariable Long courseId) throws IOException {
    FeedbackSummaryDTO summary = ratingService.getCourseRatings(courseId);
    byte[] excelFile = ratingService.exportSummaryToExcel(summary);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentDispositionFormData("attachment", "course-feedback-summary.xlsx");
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

    return ResponseEntity.ok()
            .headers(headers)
            .body(excelFile);
}
@GetMapping("/feedback-summary/form/{formId}/course/{courseId}/export-excel")
@PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
public ResponseEntity<byte[]> exportFullSummaryExcel(
        @PathVariable Long formId,
        @PathVariable Long courseId) throws IOException {

    FeedbackSummaryDTO summary = ratingService.getFullFeedbackSummary(formId, courseId);
    byte[] excelFile = ratingService.exportSummaryToExcel(summary);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentDispositionFormData("attachment", "full-feedback-summary.xlsx");
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

    return ResponseEntity.ok()
            .headers(headers)
            .body(excelFile);
}

}
