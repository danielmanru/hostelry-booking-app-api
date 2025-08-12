package com.daytoday.hostelrybooking.request;

import com.daytoday.hostelrybooking.enums.BookingStatusEnum;
import com.daytoday.hostelrybooking.model.Room;
import com.daytoday.hostelrybooking.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddBookingRequest {
  private int roomCount;
  private int nightCount;
  private LocalDate checkInDate;
  private LocalDate checkOutDate;
  private Boolean isForMe;
  private Integer guestCount;
  private String guestName;
}
