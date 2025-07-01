package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
}