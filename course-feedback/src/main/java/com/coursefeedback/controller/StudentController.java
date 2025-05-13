package com.coursefeedback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")

public class StudentController {

@PreAuthorize("hasRole('USER')")
@GetMapping("/dashboard")
public ResponseEntity<String> getDashboard() {
    return ResponseEntity.ok("Welcome to the Student Dashboard 📚");
 }
}