package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {
    List<Property> findByCountry(String country);

    List<Property> findByCity(String city);

    List<Property> findByOwnerId(UUID userId);
}