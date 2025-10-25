package edu.pict.dbmsbackend.repository;

import edu.pict.dbmsbackend.model.Car;
import edu.pict.dbmsbackend.model.enums.CarStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByOwnerId(Long ownerId);
    List<Car> findByStatus(CarStatus status);
    
    @Query("SELECT c FROM Car c WHERE " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:minPrice IS NULL OR c.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR c.price <= :maxPrice) AND " +
           "(:make IS NULL OR LOWER(c.make) LIKE LOWER(CONCAT('%', :make, '%'))) AND " +
           "(:model IS NULL OR LOWER(c.model) LIKE LOWER(CONCAT('%', :model, '%'))) AND " +
           "(:color IS NULL OR LOWER(c.color) LIKE LOWER(CONCAT('%', :color, '%'))) AND " +
           "(:ownerId IS NULL OR c.owner.id = :ownerId)")
    Page<Car> findCarsWithFilters(
            @Param("status") CarStatus status,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("make") String make,
            @Param("model") String model,
            @Param("color") String color,
            @Param("ownerId") Long ownerId,
            Pageable pageable
    );
}
