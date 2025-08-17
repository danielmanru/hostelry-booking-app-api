package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.PaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, UUID> {}