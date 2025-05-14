package com.coursefeedback.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleCourseDTO {
    private Long id;
    private String name;
}
