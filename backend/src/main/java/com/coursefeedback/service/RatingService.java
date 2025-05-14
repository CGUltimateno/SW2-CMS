package com.coursefeedback.service;
import org.apache.poi.ss.usermodel.Sheet;

import com.coursefeedback.dto.FeedbackSummaryDTO;
import com.coursefeedback.dto.FeedbackSummaryDTO.QuestionSummary;
import com.coursefeedback.entity.*;
import com.coursefeedback.repository.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final CourseRepository courseRepo;
    private final FeedbackResponseRepository responseRepo;
    private final UserRepository userRepo;
    private final FeedbackFormRepository formRepo;
    public FeedbackSummaryDTO getCourseRatings(Long courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<FeedbackResponse> responses = responseRepo.findAll().stream()
                .filter(r -> r.getCourse().getId().equals(courseId))
                .toList();

        if (responses.isEmpty()) {
            return FeedbackSummaryDTO.builder()
                    .courseName(course.getName())
                    .formTitle("No feedback submitted")
                    .totalResponses(0)
                    .questions(List.of())
                    .build();
        }

        FeedbackForm form = responses.get(0).getForm(); // Assume all same form

        List<QuestionSummary> questionSummaries = form.getQuestions().stream()
                .filter(q -> q.isRatingQuestion() && q.getTarget() == QuestionTarget.COURSE)

                .map(q -> {
                    List<FeedbackAnswer> answers = responses.stream()
                            .flatMap(r -> r.getAnswers().stream())
                            .filter(a -> a.getQuestion().getId().equals(q.getId()))
                            .toList();

                    double avg = answers.stream()
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
                }).toList();

        return FeedbackSummaryDTO.builder()
                .courseName(course.getName())
                .formTitle(form.getTitle())
                .totalResponses(responses.size())
                .questions(questionSummaries)
                .build();
    }
    








    
    public FeedbackSummaryDTO getInstructorRatings(Long instructorId) {
        User instructor = userRepo.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        List<Course> courses = courseRepo.findByInstructor(instructor);

        List<QuestionSummary> allSummaries = new ArrayList<>();
        int totalResponses = 0;

        for (Course course : courses) {
            List<FeedbackResponse> responses = responseRepo.findAll().stream()
                    .filter(r -> r.getCourse().getId().equals(course.getId()))
                    .toList();

            totalResponses += responses.size();

            if (responses.isEmpty()) continue;

            FeedbackForm form = responses.get(0).getForm();

            List<QuestionSummary> summaries = form.getQuestions().stream()
                    .filter(q -> q.isRatingQuestion() && q.getTarget() == QuestionTarget.INSTRUCTOR)

                    .map(q -> {
                        List<FeedbackAnswer> answers = responses.stream()
                                .flatMap(r -> r.getAnswers().stream())
                                .filter(a -> a.getQuestion().getId().equals(q.getId()))
                                .toList();

                        double avg = answers.stream()
                                .filter(a -> a.getRating() != null)
                                .mapToInt(FeedbackAnswer::getRating)
                                .average()
                                .orElse(0.0);

                        return QuestionSummary.builder()
                                .questionText(q.getQuestionText() + " (Course: " + course.getName() + ")")
                                .isRatingQuestion(true)
                                .averageRating(avg)
                                .textAnswers(List.of())
                                .build();
                    }).toList();

            allSummaries.addAll(summaries);
        }

        return FeedbackSummaryDTO.builder()
                .courseName("Instructor: " + instructor.getFirstName() + " " + instructor.getLastName())
                .formTitle("Aggregate Rating Summary")
                .totalResponses(totalResponses)
                .questions(allSummaries)
                .build();
    }
    public FeedbackSummaryDTO getFullFeedbackSummary(Long formId, Long courseId) {
    Course course = courseRepo.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

    FeedbackForm form = formRepo.findById(formId)
            .orElseThrow(() -> new RuntimeException("Form not found"));

    List<FeedbackResponse> responses = responseRepo.findByFormAndCourse(form, course);
    int totalResponses = responses.size();

    List<FeedbackSummaryDTO.QuestionSummary> questionSummaries = form.getQuestions().stream()
            .map(q -> {
                List<FeedbackAnswer> answers = responses.stream()
                        .flatMap(r -> r.getAnswers().stream())
                        .filter(a -> a.getQuestion().getId().equals(q.getId()))
                        .toList();

                Double avgRating = q.isRatingQuestion()
                        ? answers.stream().filter(a -> a.getRating() != null)
                          .mapToInt(FeedbackAnswer::getRating)
                          .average().orElse(0.0)
                        : null;

                List<String> textAnswers = !q.isRatingQuestion()
                        ? answers.stream().map(FeedbackAnswer::getTextAnswer)
                          .filter(Objects::nonNull).toList()
                        : List.of();

                return FeedbackSummaryDTO.QuestionSummary.builder()
                        .questionText(q.getQuestionText())
                        .isRatingQuestion(q.isRatingQuestion())
                        .averageRating(avgRating)
                        .textAnswers(textAnswers)
                        .target(q.getTarget().name())
                        .build();
            }).toList();

    return FeedbackSummaryDTO.builder()
            .courseName(course.getName())
            .formTitle(form.getTitle())
            .totalResponses(totalResponses)
            .questions(questionSummaries)
            .build();
}

    public double calculateInstructorRating(Long instructorId) {
    User instructor = userRepo.findById(instructorId)
            .orElseThrow(() -> new RuntimeException("Instructor not found"));

    List<Course> courses = courseRepo.findByInstructor(instructor);

    List<FeedbackAnswer> instructorAnswers = courses.stream()
        .flatMap(course -> responseRepo.findByCourse(course).stream())
        .flatMap(resp -> resp.getAnswers().stream())
        .filter(a -> a.getRating() != null &&
                     a.getQuestion().isRatingQuestion() &&
                     a.getQuestion().getTarget() == QuestionTarget.INSTRUCTOR)
        .toList();

    return instructorAnswers.stream()
            .mapToInt(FeedbackAnswer::getRating)
            .average()
            .orElse(0.0);
}

public byte[] exportSummaryToExcel(FeedbackSummaryDTO summary) throws IOException {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Feedback Summary");

    int rowNum = 0;

    // Meta row
    Row meta = sheet.createRow(rowNum++);
    meta.createCell(0).setCellValue("Summary for: " + summary.getCourseName());
    rowNum++; // space

    // Header
    Row header = sheet.createRow(rowNum++);
    header.createCell(0).setCellValue("Question");
    header.createCell(1).setCellValue("Is Rating?");
    header.createCell(2).setCellValue("Average Rating");
    header.createCell(3).setCellValue("Text Answers");

    if (summary.getQuestions().isEmpty()) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue("No feedback submitted.");
    } else {
        for (FeedbackSummaryDTO.QuestionSummary q : summary.getQuestions()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(q.getQuestionText());
            row.createCell(1).setCellValue(q.isRatingQuestion() ? "Yes" : "No");
            row.createCell(2).setCellValue(q.getAverageRating() != null ? q.getAverageRating() : 0);
            row.createCell(3).setCellValue(String.join(" | ", q.getTextAnswers()));
        }
    }

    // Auto-size columns
    for (int i = 0; i < 4; i++) {
        sheet.autoSizeColumn(i);
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    workbook.write(out);
    workbook.close();
    return out.toByteArray();
}



}
