package com.coursefeedback.service;
import com.coursefeedback.entity.Role;
import com.coursefeedback.dto.RegisterRequest;
import com.coursefeedback.dto.LoginRequest;
import com.coursefeedback.dto.AuthResponse;
import com.coursefeedback.entity.User;
import com.coursefeedback.repository.UserRepository;
import com.coursefeedback.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository userRepo,
                       AuthenticationManager authManager,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       UserDetailsService userDetailsService) {
        this.userRepo = userRepo;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

public void register(RegisterRequest req) {
    if (userRepo.existsByUsername(req.getUsername())) {
        throw new RuntimeException("Username is already taken");
    }
    if (userRepo.existsByEmail(req.getEmail())) {
        throw new RuntimeException("Email is already in use");
    }
    
    User user = new User();
    user.setUsername(req.getUsername());
    user.setEmail(req.getEmail());
    user.setPassword(passwordEncoder.encode(req.getPassword()));
    user.setFirstName(req.getFirstName());
    user.setLastName(req.getLastName());
    user.setDateOfBirth(req.getDateOfBirth());
    user.setPhoneNumber(req.getPhoneNumber());
    
    // Set role based on request
    try {
        Role userRole = Role.valueOf(req.getRole());
        user.setRole(userRole);
    } catch (IllegalArgumentException e) {
        user.setRole(Role.ROLE_USER);
    }

    userRepo.save(user);
}

    public AuthResponse login(LoginRequest req) {
    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
    );
    UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsername());
    String token = jwtUtil.generateToken(userDetails.getUsername());

    User user = userRepo.findByUsername(req.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found"));

    return new AuthResponse(token, user.getRole().name());
    }
}