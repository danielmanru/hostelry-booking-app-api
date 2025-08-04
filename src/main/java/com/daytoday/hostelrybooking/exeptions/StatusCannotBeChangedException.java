package com.daytoday.hostelrybooking.exeptions;

public class StatusCannotBeChangedException extends RuntimeException {
  public StatusCannotBeChangedException(String message) {
    super(message);
  }
}
