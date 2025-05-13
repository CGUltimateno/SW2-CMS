package com.coursefeedback.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "feedback_forms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FeedbackQuestion> questions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "form_course_assignments",
        joinColumns = @JoinColumn(name = "form_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> assignedCourses = new HashSet<>();
}
