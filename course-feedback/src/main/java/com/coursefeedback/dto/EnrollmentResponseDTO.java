package com.coursefeedback.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponseDTO {
    private Long id;
    private LocalDateTime enrolledAt;
    private CourseResponseDTO course;
    private StudentDTO student;
}
