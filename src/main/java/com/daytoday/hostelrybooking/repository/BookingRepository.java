package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Booking;
import com.daytoday.hostelrybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
  List<Booking> findByUser(User user);

  List<Booking> findByRoomId(UUID roomId);
}