package com.daytoday.hostelrybooking.model;

import com.daytoday.hostelrybooking.enums.PaymentMethodEnum;
import com.daytoday.hostelrybooking.enums.PaymentStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Payment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod;
    private String paymentUrl;
    private LocalDateTime expiresAt;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private PaymentReceipt paymentReceipt;

    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum status;


    public Payment(Booking booking, BigDecimal totalAmount, PaymentMethodEnum paymentMethod, String paymentUrl,LocalDateTime expiresAt, PaymentStatusEnum status) {
        this.booking = booking;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentUrl = paymentUrl;
        this.expiresAt = expiresAt;
        this.status = status;
    }
}
