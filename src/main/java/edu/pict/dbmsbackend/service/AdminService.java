package edu.pict.dbmsbackend.service;

import edu.pict.dbmsbackend.model.User;
import edu.pict.dbmsbackend.repository.UserRepository;
import edu.pict.dbmsbackend.repository.CarRepository;
import edu.pict.dbmsbackend.repository.BookingRepository;
import edu.pict.dbmsbackend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Map<String, Object> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", userRepository.count());
        stats.put("totalCars", carRepository.count());
        stats.put("totalBookings", bookingRepository.count());
        stats.put("totalReviews", reviewRepository.count());
        
        return stats;
    }
}
