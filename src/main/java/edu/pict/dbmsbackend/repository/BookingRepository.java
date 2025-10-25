package edu.pict.dbmsbackend.repository;

import edu.pict.dbmsbackend.model.Booking;
import edu.pict.dbmsbackend.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findByCarOwnerId(Long ownerId);
    
    @Query("SELECT b FROM Booking b WHERE " +
           "(:customerId IS NULL OR b.customer.id = :customerId) AND " +
           "(:ownerId IS NULL OR b.car.owner.id = :ownerId) AND " +
           "(:status IS NULL OR b.status = :status)")
    List<Booking> findBookingsWithFilters(
            @Param("customerId") Long customerId,
            @Param("ownerId") Long ownerId,
            @Param("status") BookingStatus status
    );
}
