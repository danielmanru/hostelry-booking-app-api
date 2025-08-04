package com.daytoday.hostelrybooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PaymentReceipt {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToOne(mappedBy = "paymentReceipt", cascade = CascadeType.ALL)
  @JoinColumn(name = "payment_id")
  private Payment payment;

  private String transactionId;
  private BigDecimal amount;
  private LocalDateTime paidAt;
}
