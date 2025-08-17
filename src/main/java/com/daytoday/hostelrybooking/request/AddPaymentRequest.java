package com.daytoday.hostelrybooking.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AddPaymentRequest {
  private BigDecimal totalAmount;
  private String paymentMethod;
}
