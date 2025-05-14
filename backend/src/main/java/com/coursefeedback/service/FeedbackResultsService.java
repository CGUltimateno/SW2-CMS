package com.coursefeedback.service;

import com.coursefeedback.dto.FeedbackResultsDTO;
import com.coursefeedback.dto.QuestionResultDTO;
import com.coursefeedback.entity.*;
import com.coursefeedback.repository.CourseRepository;
import com.coursefeedback.repository.FeedbackResponseRepository;
import com.coursefeedback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackResultsService {

    private final CourseRepository courseRepository;
    private final FeedbackResponseRepository responseRepository;
    private final UserRepository userRepository;

    public FeedbackResultsDTO getCourseResults(Long courseId, String instructorUsername) {
        // Get instructor and validate they teach this course
        User instructor = userRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        // Verify instructor teaches this course
        if (!course.getInstructor().getId().equals(instructor.getId())) {
            throw new RuntimeException("You can only view feedback for courses you teach");
        }
        
        // Get all feedback responses for this course
        List<FeedbackResponse> responses = responseRepository.findByCourse(course);
        
        // Process the responses into the results DTO
        return processFeedbackResponses(course, responses);
    }
    
    public List<FeedbackResultsDTO> getAllResultsForInstructor(String instructorUsername) {
        User instructor = userRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        
        // Get all courses taught by this instructor
        List<Course> instructorCourses = courseRepository.findByInstructor(instructor);
        
        // Process each course's feedback
        return instructorCourses.stream()
                .map(course -> {
                    List<FeedbackResponse> responses = responseRepository.findByCourse(course);
                    return processFeedbackResponses(course, responses);
                })
                .collect(Collectors.toList());
    }
    
private FeedbackResultsDTO processFeedbackResponses(Course course, List<FeedbackResponse> responses) {
    if (responses.isEmpty()) {
        return FeedbackResultsDTO.builder()
                .courseId(course.getId())
                .courseName(course.getName())
                .courseDescription(course.getDescription())
                .totalResponses(0)
                .averageRating(0)
                .ratingQuestionResults(Map.of())
                .textResponses(Map.of())
                .build();
    }
    
    // Maps to collect aggregated data
    Map<String, List<Integer>> ratingsByQuestion = new HashMap<>();
    Map<String, Map<Integer, Integer>> ratingDistributions = new HashMap<>();
    Map<String, List<String>> textResponsesByQuestion = new HashMap<>();
    
    int totalRatingCount = 0;
    int totalRatingSum = 0;
    
    // Process all answers in all responses
    for (FeedbackResponse response : responses) {
        if (response.getAnswers() == null) continue;
        
        for (FeedbackAnswer answer : response.getAnswers()) {
            // Skip null answers
            if (answer == null) continue;
            
            FeedbackQuestion question = answer.getQuestion();
            // Skip answers with null questions
            if (question == null) continue;
            
            // Handle null question text by providing a default
            String questionText = question.getQuestionText();
            if (questionText == null) {
                questionText = "Unnamed Question #" + question.getId();
            }
            
            if (question.isRatingQuestion() && answer.getRating() != null) {
                // Process rating question
                ratingsByQuestion.computeIfAbsent(questionText, k -> new ArrayList<>())
                        .add(answer.getRating());
                
                // Update rating distribution
                ratingDistributions.computeIfAbsent(questionText, k -> new HashMap<>());
                Map<Integer, Integer> distribution = ratingDistributions.get(questionText);
                distribution.put(answer.getRating(), distribution.getOrDefault(answer.getRating(), 0) + 1);
                
                // Add to overall totals
                totalRatingSum += answer.getRating();
                totalRatingCount++;
            } else if (!question.isRatingQuestion() && answer.getTextAnswer() != null) {
                // Process text question
                textResponsesByQuestion.computeIfAbsent(questionText, k -> new ArrayList<>())
                        .add(answer.getTextAnswer());
            }
        }
    }
    
    // Calculate question-specific results
    Map<String, QuestionResultDTO> questionResults = new HashMap<>();
    ratingsByQuestion.forEach((questionText, ratings) -> {
        // Skip any null keys that might have slipped through
        if (questionText == null) return;
        
        double average = ratings.stream().mapToInt(Integer::intValue).average().orElse(0);
        questionResults.put(questionText, QuestionResultDTO.builder()
                .questionText(questionText)
                .averageRating(average)
                .responseCount(ratings.size())
                .ratingDistribution(ratingDistributions.get(questionText))
                .build());
    });
    
    // Calculate overall average
    double overallAverage = totalRatingCount > 0 ? (double) totalRatingSum / totalRatingCount : 0;
    
    return FeedbackResultsDTO.builder()
            .courseId(course.getId())
            .courseName(course.getName())
            .courseDescription(course.getDescription())
            .totalResponses(responses.size())
            .averageRating(overallAverage)
            .ratingQuestionResults(questionResults)
            .textResponses(textResponsesByQuestion)
            .build();
}
    
    public byte[] generateFeedbackReport(Long courseId, String instructorUsername) {
        // Get the data in the same format as for the web view
        FeedbackResultsDTO results = getCourseResults(courseId, instructorUsername);
        
        // This is a placeholder - in a real application, you would use a PDF library
        // like iText, Apache PDFBox, or JasperReports to generate a real PDF
        String report = generateTextReport(results);
        
        return report.getBytes();
    }
    
    private String generateTextReport(FeedbackResultsDTO results) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Feedback Report for: ").append(results.getCourseName()).append("\n\n");
        sb.append("Total Responses: ").append(results.getTotalResponses()).append("\n");
        sb.append("Overall Average Rating: ").append(String.format("%.2f", results.getAverageRating())).append("/5\n\n");
        
        sb.append("=== RATING QUESTIONS ===\n\n");
        results.getRatingQuestionResults().forEach((questionText, result) -> {
            sb.append("Question: ").append(questionText).append("\n");
            sb.append("Average Rating: ").append(String.format("%.2f", result.getAverageRating())).append("/5\n");
            sb.append("Response Count: ").append(result.getResponseCount()).append("\n");
            
            sb.append("Rating Distribution:\n");
            for (int i = 1; i <= 5; i++) {
                int count = result.getRatingDistribution().getOrDefault(i, 0);
                sb.append("  ").append(i).append(" stars: ").append(count).append("\n");
            }
            sb.append("\n");
        });
        
        sb.append("=== TEXT RESPONSES ===\n\n");
        results.getTextResponses().forEach((questionText, responses) -> {
            sb.append("Question: ").append(questionText).append("\n");
            sb.append("Responses:\n");
            for (int i = 0; i < responses.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(responses.get(i)).append("\n");
            }
            sb.append("\n");
        });
        
        return sb.toString();
    }
}