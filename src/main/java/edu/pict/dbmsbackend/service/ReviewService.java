package edu.pict.dbmsbackend.service;

import edu.pict.dbmsbackend.dto.ReviewRequest;
import edu.pict.dbmsbackend.model.Car;
import edu.pict.dbmsbackend.model.Review;
import edu.pict.dbmsbackend.model.User;
import edu.pict.dbmsbackend.repository.CarRepository;
import edu.pict.dbmsbackend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CarRepository carRepository;

    public Review createReview(Long carId, ReviewRequest request, User customer) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        // Check if customer has already reviewed this car
        List<Review> existingReviews = reviewRepository.findByCarIdAndCustomerId(carId, customer.getId());
        if (!existingReviews.isEmpty()) {
            throw new RuntimeException("You have already reviewed this car");
        }

        Review review = new Review();
        review.setCar(car);
        review.setCustomer(customer);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public List<Review> getCarReviews(Long carId) {
        return reviewRepository.findByCarId(carId);
    }

    public List<Review> getCustomerReviews(Long customerId) {
        return reviewRepository.findByCustomerId(customerId);
    }
}
