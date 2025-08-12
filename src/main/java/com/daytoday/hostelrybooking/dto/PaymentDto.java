package com.daytoday.hostelrybooking.dto;

import com.daytoday.hostelrybooking.enums.PaymentMethodEnum;
import com.daytoday.hostelrybooking.enums.PaymentStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentDto {
  private UUID id;
  private BigDecimal totalAmount;
  private PaymentMethodEnum paymentMethod;
  private String paymentUrl;
  private LocalDateTime expiresAt;
  private PaymentReceiptDto paymentReceipt;
  private PaymentStatusEnum status;
}
