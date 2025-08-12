package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AmenityRepository extends JpaRepository<Amenity, UUID> {
  List<Amenity> findByCategory(String category);

  boolean existsByName(String name);

  boolean existsByCategoryAndName(String category, String name);
}