package com.daytoday.hostelrybooking.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AddPaymentRequest {
  private UUID bookingId;
  private BigDecimal totalAmount;
  private String paymentMethod;
}
