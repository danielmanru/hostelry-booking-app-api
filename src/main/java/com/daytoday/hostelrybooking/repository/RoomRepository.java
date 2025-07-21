package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    List<Room> findByPropertyId(UUID propertyId);
}