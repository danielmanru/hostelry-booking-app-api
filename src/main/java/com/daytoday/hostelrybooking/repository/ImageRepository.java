package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
  List<Image> findByPropertyId(Long propertyId);

  List<Image> findByRoomId(Long roomId);

  Optional<Image> findByFilePublicId(String filePublicId);
}