package com.coursefeedback.service;
import java.util.stream.Collectors;
import com.coursefeedback.dto.CourseResponseDTO;
import com.coursefeedback.dto.CourseWithFeedbackDTO;
import com.coursefeedback.dto.InstructorDTO;
import com.coursefeedback.entity.Course;
import com.coursefeedback.entity.Enrollment;
import com.coursefeedback.entity.FeedbackForm;
import com.coursefeedback.entity.User;
import com.coursefeedback.repository.CourseRepository;
import com.coursefeedback.repository.EnrollmentRepository;
import com.coursefeedback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {

    private final CourseRepository courseRepo;
    private final UserRepository userRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final FeedbackFormService feedbackFormService; 
    private final FeedbackResponseService feedbackResponseService; 


    @Override
   public CourseResponseDTO createCourse(Course course, String instructorUsername) {
        User instructor = userRepo.findByUsername(instructorUsername)
            .orElseThrow(() -> new RuntimeException("Instructor not found"));

        course.setInstructor(instructor);
        Course savedCourse = courseRepo.save(course);
        return mapToDTO(savedCourse);
    }

    @Override
    public List<CourseResponseDTO> getCoursesByInstructor(String username) {
        User instructor = userRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Instructor not found"));

        List<Course> courses = courseRepo.findByInstructor(instructor);
        return courses.stream()
                      .map(this::mapToDTO)
                      .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponseDTO> getAllCourses() {
        List<Course> courses = courseRepo.findAll();
        return courses.stream()
                      .map(this::mapToDTO)
                      .collect(Collectors.toList());
    }

    @Override
    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return mapToDTO(course);
    }
    
private CourseResponseDTO mapToDTO(Course course) {
    InstructorDTO instructor = InstructorDTO.builder()
        .id(course.getInstructor().getId())
        .username(course.getInstructor().getUsername())
        .firstName(course.getInstructor().getFirstName())
        .lastName(course.getInstructor().getLastName())
        .build();

    return CourseResponseDTO.builder()
        .id(course.getId())
        .name(course.getName())
        .description(course.getDescription())
        .startDate(course.getStartDate())
        .endDate(course.getEndDate())
        .capacity(course.getCapacity())
        .instructor(instructor)
        .build();
}

    @Override
    public List<CourseWithFeedbackDTO> getEnrolledCoursesWithFeedbackInfo(String username) {
        User student = userRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        List<Enrollment> enrollments = enrollmentRepo.findByStudent(student);
        
        return enrollments.stream()
            .map(enrollment -> {
                Course course = enrollment.getCourse();
                
                boolean hasOpenFeedback = false;
                Long openFeedbackFormId = null;
                
                List<FeedbackForm> assignedForms = feedbackFormService.getFormsAssignedToCourse(course.getId());
                
                for (FeedbackForm form : assignedForms) {
                    boolean alreadySubmitted = feedbackResponseService.hasStudentSubmittedFeedback(
                        student.getId(), form.getId());
                    
                    if (!alreadySubmitted) {
                        hasOpenFeedback = true;
                        openFeedbackFormId = form.getId();
                        break;
                    }
                }
                
                InstructorDTO instructorDTO = InstructorDTO.builder()
                    .id(course.getInstructor().getId())
                    .username(course.getInstructor().getUsername())
                    .firstName(course.getInstructor().getFirstName())
                    .lastName(course.getInstructor().getLastName())
                    .build();
                    
                return CourseWithFeedbackDTO.builder()
                    .id(course.getId())
                    .name(course.getName())
                    .description(course.getDescription())
                    .startDate(course.getStartDate())
                    .endDate(course.getEndDate())
                    .capacity(course.getCapacity())
                    .instructor(instructorDTO)
                    .hasOpenFeedback(hasOpenFeedback)
                    .openFeedbackFormId(openFeedbackFormId)
                    .build();
            })
            .collect(Collectors.toList());
    }
}