package edu.pict.dbmsbackend.repository;

import edu.pict.dbmsbackend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCarId(Long carId);
    List<Review> findByCustomerId(Long customerId);
    List<Review> findByCarIdAndCustomerId(Long carId, Long customerId);
}
