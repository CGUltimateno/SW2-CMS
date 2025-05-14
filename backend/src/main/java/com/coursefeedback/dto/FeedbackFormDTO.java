package com.coursefeedback.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


import com.coursefeedback.dto.SimpleCourseDTO;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackFormDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private List<FeedbackQuestionDTO> questions;
    private List<SimpleCourseDTO> assignedCourses;
}
