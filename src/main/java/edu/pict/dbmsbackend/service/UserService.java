package edu.pict.dbmsbackend.service;

import edu.pict.dbmsbackend.dtos.LoginDto;
import edu.pict.dbmsbackend.dtos.RegisterDto;
import edu.pict.dbmsbackend.model.User;
import edu.pict.dbmsbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String registerDto(RegisterDto registerDto) {
        User user = User.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .phone("") // Default phone if not provided
                .role(registerDto.getRole())
                .build();
        log.info("registerDto:{}", registerDto);
        try {
            User saved = userRepository.save(user);
            return saved.getEmail();
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage());
            return e.getMessage();
        }
    }

    @Transactional
    public User loginDto(LoginDto loginDto) {
        return userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
