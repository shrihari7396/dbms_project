package edu.pict.dbmsbackend.controller;

import edu.pict.dbmsbackend.dto.ReviewRequest;
import edu.pict.dbmsbackend.model.Review;
import edu.pict.dbmsbackend.model.User;
import edu.pict.dbmsbackend.service.AuthService;
import edu.pict.dbmsbackend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars/{carId}/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<Review>> getCarReviews(@PathVariable Long carId) {
        List<Review> reviews = reviewService.getCarReviews(carId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Review> createReview(@PathVariable Long carId, 
                                             @Valid @RequestBody ReviewRequest request, 
                                             Authentication authentication) {
        try {
            String email = authentication.getName();
            User customer = authService.getCurrentUser(email);
            Review review = reviewService.createReview(carId, request, customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
