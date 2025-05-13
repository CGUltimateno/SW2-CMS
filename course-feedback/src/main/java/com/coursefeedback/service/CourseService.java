package com.coursefeedback.service;
import java.util.stream.Collectors;
import com.coursefeedback.dto.CourseResponseDTO;
import com.coursefeedback.dto.InstructorDTO;
import com.coursefeedback.entity.Course;
import com.coursefeedback.entity.User;
import com.coursefeedback.repository.CourseRepository;
import com.coursefeedback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {

    private final CourseRepository courseRepo;
    private final UserRepository userRepo;

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
            .instructor(instructor)
            .build();
    }
}