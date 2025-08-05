package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Review;
import com.daytoday.hostelrybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
  List<Review> findByPropertyId(UUID propertyId);

  List<Review> findByUser(User user);
}