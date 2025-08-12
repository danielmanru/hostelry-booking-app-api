package com.daytoday.hostelrybooking.dto;

import com.daytoday.hostelrybooking.enums.BookingStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingDto {
  private UUID id;
  private RoomDto room;
  private int roomCount;
  private int nightCount;
  private LocalDate checkInDate;
  private LocalDate checkOutDate;
  private Boolean isForMe;
  private int guestCount;
  private String guestName;
  private BigDecimal totalAmount;
  private BookingStatusEnum status;
  private PaymentDto payment;
}
