package edu.pict.dbmsbackend.service;

import edu.pict.dbmsbackend.dto.BookingRequest;
import edu.pict.dbmsbackend.model.Booking;
import edu.pict.dbmsbackend.model.Car;
import edu.pict.dbmsbackend.model.User;
import edu.pict.dbmsbackend.model.enums.BookingStatus;
import edu.pict.dbmsbackend.model.enums.CarStatus;
import edu.pict.dbmsbackend.repository.BookingRepository;
import edu.pict.dbmsbackend.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;

    public Booking createBooking(BookingRequest request, User customer, long days) {
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (car.getStatus() != CarStatus.FREE) {
            throw new RuntimeException("Car is not available");
        }

        double totalCost = car.getPrice() * days;

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setCar(car);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setTotalCost(totalCost);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        // Update car status to booked
        car.setStatus(CarStatus.BOOKED);
        carRepository.save(car);

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsWithFilters(Long customerId, Long ownerId, BookingStatus status, User user) {
        // If user is admin, they can see all bookings
        if (user.getRole().name().equals("ADMIN")) {
            return bookingRepository.findBookingsWithFilters(customerId, ownerId, status);
        }
        
        // If user is owner, they can see bookings for their cars
        if (user.getRole().name().equals("OWNER")) {
            return bookingRepository.findBookingsWithFilters(null, user.getId(), status);
        }
        
        // If user is customer, they can see their own bookings
        return bookingRepository.findBookingsWithFilters(user.getId(), null, status);
    }
}
