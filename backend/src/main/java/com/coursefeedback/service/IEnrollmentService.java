package com.coursefeedback.service;


import com.coursefeedback.entity.Enrollment;

import java.security.Principal;
import java.util.List;

public interface IEnrollmentService {
    void enroll(Long courseId, Principal principal);
    List<Enrollment> getStudentEnrollments(Principal principal);
}
