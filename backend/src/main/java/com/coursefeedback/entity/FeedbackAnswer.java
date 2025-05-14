package com.coursefeedback.entity;
import lombok.*;

import java.util.List;
import java.time.LocalDateTime;
import com.coursefeedback.entity.FeedbackQuestion;
import com.coursefeedback.entity.FeedbackResponse;
import com.coursefeedback.entity.FeedbackForm;
import com.coursefeedback.entity.Course;
import com.coursefeedback.entity.FeedbackAnswer;
import jakarta.persistence.*;

@Entity
@Table(name = "feedback_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating;  // Optional
    private String textAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private FeedbackQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id")
    private FeedbackResponse response;
}
