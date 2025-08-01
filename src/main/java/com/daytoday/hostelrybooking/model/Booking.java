package com.daytoday.hostelrybooking.model;

import com.daytoday.hostelrybooking.enums.BookingStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    private int roomCount;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private Boolean isForMe;
    private int guestCount;
    private String guestName;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status;

    @OneToOne
    private Payment payment;
}
