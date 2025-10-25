package edu.pict.dbmsbackend.config;

import edu.pict.dbmsbackend.model.User;
import edu.pict.dbmsbackend.model.Car;
import edu.pict.dbmsbackend.model.enums.CarStatus;
import edu.pict.dbmsbackend.model.enums.Role;
import edu.pict.dbmsbackend.repository.UserRepository;
import edu.pict.dbmsbackend.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedUsers();
            seedCars();
        }
    }

    private void seedUsers() {
        // Create Admin User
        User admin = User.builder()
                .name("Admin User")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .phone("1234567890")
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(admin);

        // Create Owner User
        User owner = User.builder()
                .name("Car Owner")
                .email("owner@example.com")
                .password(passwordEncoder.encode("owner123"))
                .phone("1234567891")
                .role(Role.OWNER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(owner);

        // Create Customer User
        User customer = User.builder()
                .name("John Customer")
                .email("customer@example.com")
                .password(passwordEncoder.encode("customer123"))
                .phone("1234567892")
                .role(Role.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(customer);
    }

    private void seedCars() {
        User owner = userRepository.findByEmail("owner@example.com").orElse(null);
        if (owner == null) return;

        List<Car> cars = Arrays.asList(
            Car.builder()
                .make("Toyota")
                .model("Camry")
                .year(2022)
                .color("Silver")
                .price(50.0)
                .status(CarStatus.FREE)
                .owner(owner)
                .image("https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=400")
                .description("Comfortable and reliable sedan")
                .features(Arrays.asList("AC", "Bluetooth", "GPS", "Airbags"))
                .mileage(15000)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(),

            Car.builder()
                .make("Honda")
                .model("Civic")
                .year(2021)
                .color("Blue")
                .price(45.0)
                .status(CarStatus.FREE)
                .owner(owner)
                .image("https://images.unsplash.com/photo-1550355291-bbee04a92027?w=400")
                .description("Fuel-efficient compact car")
                .features(Arrays.asList("AC", "Bluetooth", "USB Port"))
                .mileage(25000)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(),

            Car.builder()
                .make("BMW")
                .model("X5")
                .year(2023)
                .color("Black")
                .price(120.0)
                .status(CarStatus.BOOKED)
                .owner(owner)
                .image("https://images.unsplash.com/photo-1555215695-3004980ad54e?w=400")
                .description("Luxury SUV with premium features")
                .features(Arrays.asList("AC", "Bluetooth", "GPS", "Leather Seats", "Sunroof"))
                .mileage(8000)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(),

            Car.builder()
                .make("Ford")
                .model("Focus")
                .year(2020)
                .color("Red")
                .price(35.0)
                .status(CarStatus.DEFECTIVE)
                .owner(owner)
                .image("https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=400")
                .description("Economical choice for city driving")
                .features(Arrays.asList("AC", "Bluetooth"))
                .mileage(35000)
                .issues(Arrays.asList("Engine check light on", "AC not working properly"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );

        carRepository.saveAll(cars);
    }
}
