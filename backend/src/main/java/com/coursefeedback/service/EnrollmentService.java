package com.coursefeedback.service;

import com.coursefeedback.entity.*;
import com.coursefeedback.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService implements IEnrollmentService {

    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;

    @Override
    public void enroll(Long courseId, Principal principal) {
        User student = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (student.getRole() != Role.ROLE_USER) {
            throw new RuntimeException("Only students can enroll in courses.");
        }

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        boolean alreadyEnrolled = enrollmentRepo.findByStudentAndCourse(student, course).isPresent();
        if (alreadyEnrolled) {
            throw new RuntimeException("Already enrolled in this course.");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .enrolledAt(LocalDateTime.now())
                .build();

        enrollmentRepo.save(enrollment);
    }

    @Override
    public List<Enrollment> getStudentEnrollments(Principal principal) {
        User student = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return enrollmentRepo.findByStudent(student);
    }
}
