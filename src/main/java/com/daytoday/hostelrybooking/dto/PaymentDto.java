package com.daytoday.hostelrybooking.dto;

import com.daytoday.hostelrybooking.enums.PaymentMethodEnum;
import com.daytoday.hostelrybooking.enums.PaymentStatusEnum;
import com.daytoday.hostelrybooking.model.Booking;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PaymentDto {
  private UUID id;
  private String transactionId;
  private BigDecimal totalAmount;
  private PaymentMethodEnum paymentMethod;
  private PaymentStatusEnum status;
  private LocalDate paidAt;
}
