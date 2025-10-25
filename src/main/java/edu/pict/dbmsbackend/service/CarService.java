package edu.pict.dbmsbackend.service;

import edu.pict.dbmsbackend.dto.CarRequest;
import edu.pict.dbmsbackend.model.Car;
import edu.pict.dbmsbackend.model.User;
import edu.pict.dbmsbackend.model.enums.CarStatus;
import edu.pict.dbmsbackend.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public Page<Car> getCarsWithFilters(CarStatus status, Double minPrice, Double maxPrice, 
                                       String make, String model, String color, Long ownerId, Pageable pageable) {
        return carRepository.findCarsWithFilters(status, minPrice, maxPrice, make, model, color, ownerId, pageable);
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));
    }

    public Car createCar(CarRequest request, User owner) {
        Car car = new Car();
        car.setMake(request.getMake());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setColor(request.getColor());
        car.setPrice(request.getPrice());
        car.setStatus(request.getStatus() != null ? request.getStatus() : CarStatus.FREE);
        car.setImage(request.getImage());
        car.setDescription(request.getDescription());
        car.setFeatures(request.getFeatures());
        car.setMileage(request.getMileage());
        car.setIssues(request.getIssues());
        car.setOwner(owner);
        car.setCreatedAt(LocalDateTime.now());
        car.setUpdatedAt(LocalDateTime.now());

        return carRepository.save(car);
    }

    public Car updateCar(Long id, CarRequest request, User user) {
        Car car = getCarById(id);
        
        // Check if user is owner or admin
        if (!car.getOwner().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Not authorized to update this car");
        }

        car.setMake(request.getMake());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setColor(request.getColor());
        car.setPrice(request.getPrice());
        if (request.getStatus() != null) {
            car.setStatus(request.getStatus());
        }
        car.setImage(request.getImage());
        car.setDescription(request.getDescription());
        car.setFeatures(request.getFeatures());
        car.setMileage(request.getMileage());
        car.setIssues(request.getIssues());
        car.setUpdatedAt(LocalDateTime.now());

        return carRepository.save(car);
    }

    public void deleteCar(Long id, User user) {
        Car car = getCarById(id);
        
        // Check if user is owner or admin
        if (!car.getOwner().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Not authorized to delete this car");
        }

        carRepository.delete(car);
    }

    public List<Car> getCarsByOwner(Long ownerId) {
        return carRepository.findByOwnerId(ownerId);
    }
}
