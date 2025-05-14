package com.coursefeedback.controller;

import static com.coursefeedback.mapper.coursemapper.mapToDTO;
import com.coursefeedback.dto.CourseResponseDTO;
import com.coursefeedback.dto.CourseWithFeedbackDTO; 
import com.coursefeedback.entity.Course;
import com.coursefeedback.mapper.coursemapper;
import com.coursefeedback.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody Course course, Principal principal) {
        CourseResponseDTO savedCourse = courseService.createCourse(course, principal.getName());
        return ResponseEntity.ok(savedCourse);
    }

    @GetMapping("/instructor")
    @PreAuthorize("hasRole('INSTRUCTOR')")
     public ResponseEntity<List<CourseResponseDTO>> getInstructorCourses(Principal principal) {
        // Directly use the returned list of DTOs without mapping
        List<CourseResponseDTO> response = courseService.getCoursesByInstructor(principal.getName());
        return ResponseEntity.ok(response);
        
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'USER')")
    @GetMapping
 public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        // Directly use the returned list of DTOs without mapping
        List<CourseResponseDTO> response = courseService.getAllCourses();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable Long id) {
        // Directly return the DTO from the service without additional mapping
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PreAuthorize("hasRole('USER')")
@GetMapping("/enrolled-with-feedback")
public ResponseEntity<List<CourseWithFeedbackDTO>> getEnrolledCoursesWithFeedbackInfo(Principal principal) {
    List<CourseWithFeedbackDTO> coursesWithFeedback = courseService.getEnrolledCoursesWithFeedbackInfo(principal.getName());
    return ResponseEntity.ok(coursesWithFeedback);
}
}