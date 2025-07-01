package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByReviewRating(int rating);

    List<Property> findByCountry(String country);

    List<Property> findByCity(String city);

    List<Property> findByOwner(Long userId);
}