package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
  List<Payment> findByBookingRoomId();
}