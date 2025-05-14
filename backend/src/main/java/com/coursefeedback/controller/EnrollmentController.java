package com.coursefeedback.controller;


import com.coursefeedback.mapper.EnrollmentMapper;
import com.coursefeedback.entity.Enrollment;
import com.coursefeedback.service.IEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.coursefeedback.dto.EnrollmentResponseDTO;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final IEnrollmentService enrollmentService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<String> enroll(@PathVariable Long courseId, Principal principal) {
        enrollmentService.enroll(courseId, principal);
        return ResponseEntity.ok("Enrollment successful ✅");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
   public ResponseEntity<List<EnrollmentResponseDTO>> getStudentEnrollments(Principal principal) {
    List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(principal);

    List<EnrollmentResponseDTO> dtos = enrollments.stream()
            .map(EnrollmentMapper::mapToDTO)
            .toList();

    return ResponseEntity.ok(dtos);
}
}
