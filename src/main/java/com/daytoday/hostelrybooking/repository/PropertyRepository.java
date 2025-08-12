package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID>, JpaSpecificationExecutor<Property> {
  List<Property> findByCountry(String country);

  List<Property> findByCity(String city);

  List<Property> findByOwnerId(UUID userId);

  boolean existsByOwnerAndName(User user, String propertyName);
}