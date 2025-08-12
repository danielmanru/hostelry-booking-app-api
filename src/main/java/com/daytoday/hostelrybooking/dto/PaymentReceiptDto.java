package com.daytoday.hostelrybooking.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentReceiptDto {
  private UUID id;
  private String transactionId;
  private BigDecimal amount;
  private LocalDateTime paidAt;
}
