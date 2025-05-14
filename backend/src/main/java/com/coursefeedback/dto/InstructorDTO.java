package com.coursefeedback.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
}

