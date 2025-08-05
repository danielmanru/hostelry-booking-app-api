package com.daytoday.hostelrybooking.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AddPaymentReceiptRequest {
  private String transactionId;
  private BigDecimal amount;
  private LocalDateTime paidAt;
}
