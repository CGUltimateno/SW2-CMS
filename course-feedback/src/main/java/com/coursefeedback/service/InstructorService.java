package com.coursefeedback.service;

import com.coursefeedback.dto.FeedbackSummaryDTO;
import com.coursefeedback.dto.FeedbackSummaryDTO.QuestionSummary;
import com.coursefeedback.entity.*;
import com.coursefeedback.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final FeedbackResponseRepository responseRepo;

    public FeedbackSummaryDTO getFeedbackSummary(Long courseId, String instructorUsername) {
        User instructor = userRepo.findByUsername(instructorUsername)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!Objects.equals(course.getInstructor().getId(), instructor.getId())) {
            throw new RuntimeException("Access denied: this is not your course");
        }

        List<FeedbackResponse> responses = responseRepo.findAll().stream()
                .filter(resp -> resp.getCourse().getId().equals(courseId))
                .toList();

        if (responses.isEmpty()) {
            return FeedbackSummaryDTO.builder()
                    .courseName(course.getName())
                    .formTitle("No feedback submitted")
                    .totalResponses(0)
                    .questions(List.of())
                    .build();
        }

        FeedbackForm form = responses.get(0).getForm();

        List<QuestionSummary> questionSummaries = form.getQuestions().stream().map(q -> {
            List<FeedbackAnswer> allAnswers = responses.stream()
                    .flatMap(r -> r.getAnswers().stream())
                    .filter(a -> a.getQuestion().getId().equals(q.getId()))
                    .toList();

            if (q.isRatingQuestion()) {
                double avg = allAnswers.stream()
                        .filter(a -> a.getRating() != null)
                        .mapToInt(FeedbackAnswer::getRating)
                        .average()
                        .orElse(0.0);
                return QuestionSummary.builder()
                        .questionText(q.getQuestionText())
                        .isRatingQuestion(true)
                        .averageRating(avg)
                        .textAnswers(List.of())
                        .build();
            } else {
                List<String> texts = allAnswers.stream()
                        .map(FeedbackAnswer::getTextAnswer)
                        .filter(Objects::nonNull)
                        .toList();
                return QuestionSummary.builder()
                        .questionText(q.getQuestionText())
                        .isRatingQuestion(false)
                        .averageRating(null)
                        .textAnswers(texts)
                        .build();
            }
        }).toList();

        return FeedbackSummaryDTO.builder()
                .courseName(course.getName())
                .formTitle(form.getTitle())
                .totalResponses(responses.size())
                .questions(questionSummaries)
                .build();
    }
}
