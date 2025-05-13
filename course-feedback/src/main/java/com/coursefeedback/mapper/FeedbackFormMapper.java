package com.coursefeedback.mapper;
import com.coursefeedback.dto.SimpleCourseDTO;
import com.coursefeedback.dto.FeedbackFormDTO;
import com.coursefeedback.dto.FeedbackQuestionDTO;

import com.coursefeedback.dto.FeedbackFormRequest;
import com.coursefeedback.dto.*;
import com.coursefeedback.entity.FeedbackForm;
import com.coursefeedback.entity.FeedbackQuestion;
import java.util.stream.Collectors;

public class FeedbackFormMapper {

    public static FeedbackFormDTO toDTO(FeedbackForm form) {
        return FeedbackFormDTO.builder()
                .id(form.getId())
                .title(form.getTitle())
                .description(form.getDescription())
                .questions(form.getQuestions().stream()
                    .map(FeedbackFormMapper::mapQuestionToDTO)
                    .collect(Collectors.toList()))
                .assignedCourses(
                        form.getAssignedCourses().stream()
                            .map(c -> SimpleCourseDTO.builder()
                                    .id(c.getId())
                                    .name(c.getName())
                                    .build())
                            .collect(Collectors.toList())
                )
                .build();
    }
    public static FeedbackQuestionDTO mapQuestionToDTO(FeedbackQuestion question) {
    return FeedbackQuestionDTO.builder()
            .id(question.getId())
            .questionText(question.getQuestionText())
            .isRatingQuestion(question.isRatingQuestion())
            .target(question.getTarget())
            .build();
}

}
