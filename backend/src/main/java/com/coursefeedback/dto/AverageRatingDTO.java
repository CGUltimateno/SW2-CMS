package com.coursefeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AverageRatingDTO {
    private String entityName;
    private double averageRating;
}
