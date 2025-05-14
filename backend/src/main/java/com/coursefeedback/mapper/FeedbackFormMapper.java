package com.coursefeedback.mapper;
import com.coursefeedback.dto.SimpleCourseDTO;
import com.coursefeedback.dto.FeedbackFormDTO;
import com.coursefeedback.dto.FeedbackQuestionDTO;
import com.coursefeedback.dto.FeedbackFormRequest;
import com.coursefeedback.entity.FeedbackForm;
import com.coursefeedback.entity.FeedbackQuestion;

import java.util.List;
import java.util.stream.Collectors;

public class FeedbackFormMapper {

    public static FeedbackFormDTO toDTO(FeedbackForm form) {
        if (form == null) return null;
        
        List<FeedbackQuestionDTO> questionDTOs = form.getQuestions() != null 
            ? form.getQuestions().stream().map(FeedbackFormMapper::mapQuestionToDTO).toList() 
            : List.of();
        
        List<SimpleCourseDTO> courseDTOs = form.getAssignedCourses() != null 
            ? form.getAssignedCourses().stream()
                .map(course -> SimpleCourseDTO.builder()
                    .id(course.getId())
                    .name(course.getName())
                    .build())
                .toList()
            : List.of();
        
        return FeedbackFormDTO.builder()
            .id(form.getId())
            .title(form.getTitle())
            .description(form.getDescription())
            .createdAt(form.getCreatedAt())
            .questions(questionDTOs)
            .assignedCourses(courseDTOs)
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