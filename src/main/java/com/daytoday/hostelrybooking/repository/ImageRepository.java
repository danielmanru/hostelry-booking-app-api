package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
  List<Image> findByPropertyId(UUID propertyId);

  List<Image> findByRoomId(UUID roomId);

  Optional<Image> findByFilePublicId(String filePublicId);
}