package com.coursefeedback.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseWithFeedbackDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer capacity;
    private InstructorDTO instructor;
    private boolean hasOpenFeedback;
    private Long openFeedbackFormId;
}