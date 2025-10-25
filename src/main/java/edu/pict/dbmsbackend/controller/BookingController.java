package edu.pict.dbmsbackend.controller;

import edu.pict.dbmsbackend.dto.BookingRequest;
import edu.pict.dbmsbackend.model.Booking;
import edu.pict.dbmsbackend.model.User;
import edu.pict.dbmsbackend.model.enums.BookingStatus;
import edu.pict.dbmsbackend.service.AuthService;
import edu.pict.dbmsbackend.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;
    private final AuthService authService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingRequest request, Authentication authentication) {
        try {
            String email = authentication.getName();
            User customer = authService.getCurrentUser(email);
            
            // Calculate total cost
            long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
            
            Booking booking = bookingService.createBooking(request, customer, days);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) BookingStatus status,
            Authentication authentication
    ) {
        try {
            String email = authentication.getName();
            User user = authService.getCurrentUser(email);
            
            List<Booking> bookings = bookingService.getBookingsWithFilters(customerId, ownerId, status, user);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}