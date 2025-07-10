package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    List<Amenity> findByCategory(String category);

    boolean existsByName(String name);
}