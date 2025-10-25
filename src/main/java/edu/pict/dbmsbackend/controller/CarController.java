package edu.pict.dbmsbackend.controller;

import edu.pict.dbmsbackend.dto.CarRequest;
import edu.pict.dbmsbackend.model.Car;
import edu.pict.dbmsbackend.model.User;
import edu.pict.dbmsbackend.model.enums.CarStatus;
import edu.pict.dbmsbackend.service.CarService;
import edu.pict.dbmsbackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars(
            @RequestParam(required = false) CarStatus status,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false, defaultValue = "nameAsc") String sortBy,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "100") int size
    ) {
        Pageable pageable = createPageable(sortBy, page, size);
        Page<Car> cars = carService.getCarsWithFilters(status, minPrice, maxPrice, make, model, color, ownerId, pageable);
        return ResponseEntity.ok(cars.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        try {
            Car car = carService.getCarById(id);
            return ResponseEntity.ok(car);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<Car> createCar(@Valid @RequestBody CarRequest request, Authentication authentication) {
        try {
            String email = authentication.getName();
            User owner = authService.getCurrentUser(email);
            Car car = carService.createCar(request, owner);
            return ResponseEntity.status(HttpStatus.CREATED).body(car);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @Valid @RequestBody CarRequest request, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = authService.getCurrentUser(email);
            Car car = carService.updateCar(id, request, user);
            return ResponseEntity.ok(car);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = authService.getCurrentUser(email);
            carService.deleteCar(id, user);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    private Pageable createPageable(String sortBy, int page, int size) {
        Sort sort;
        switch (sortBy) {
            case "priceAsc":
                sort = Sort.by(Sort.Direction.ASC, "price");
                break;
            case "priceDesc":
                sort = Sort.by(Sort.Direction.DESC, "price");
                break;
            case "yearDesc":
                sort = Sort.by(Sort.Direction.DESC, "year");
                break;
            case "ratingDesc":
                sort = Sort.by(Sort.Direction.DESC, "averageRating");
                break;
            case "nameAsc":
            default:
                sort = Sort.by(Sort.Direction.ASC, "make", "model");
                break;
        }
        return PageRequest.of(page, size, sort);
    }
}
