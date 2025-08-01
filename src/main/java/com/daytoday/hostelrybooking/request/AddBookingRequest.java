package com.daytoday.hostelrybooking.request;

import com.daytoday.hostelrybooking.model.Room;
import com.daytoday.hostelrybooking.model.User;

import java.time.LocalDate;

public class AddBookingRequest {
  private User user;
  private Room room;
  private LocalDate checkInDate;
  private LocalDate checkOutDate;
  private Boolean isForMe;
  private Integer guestCount;
  private String guestName;
}
