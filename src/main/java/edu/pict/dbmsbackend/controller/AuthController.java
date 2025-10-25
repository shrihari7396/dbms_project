package edu.pict.dbmsbackend.controller;

import edu.pict.dbmsbackend.dto.AuthResponse;
import edu.pict.dbmsbackend.dto.LoginRequest;
import edu.pict.dbmsbackend.dto.RegisterRequest;
import edu.pict.dbmsbackend.model.User;
import edu.pict.dbmsbackend.service.AuthService;
import edu.pict.dbmsbackend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            String token = jwtUtil.generateToken(user.getEmail());
            
            AuthResponse response = new AuthResponse(token, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = authService.login(request);
            String token = jwtUtil.generateToken(user.getEmail());
            
            AuthResponse response = new AuthResponse(token, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = authService.getCurrentUser(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
