package com.daytoday.hostelrybooking.exeptions;

public class NotEnoughRoomsException extends RuntimeException {
  public NotEnoughRoomsException(String message) {
    super(message);
  }
}
