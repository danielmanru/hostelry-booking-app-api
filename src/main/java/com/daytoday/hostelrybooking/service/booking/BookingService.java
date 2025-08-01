package com.daytoday.hostelrybooking.service.booking;

import com.daytoday.hostelrybooking.model.Booking;
import com.daytoday.hostelrybooking.request.CreateBookingRequest;

import java.util.List;

public class BookingService implements IBookingService {

  @Override
  public Booking createBooking(CreateBookingRequest request) {
    return null;
  }

  @Override
  public List<Booking> getUserBookings() {
    return List.of();
  }

  @Override
  public Booking getBookingById(Long bookingId) {
    return null;
  }

  @Override
  public Booking updateBookingStatus(String bookingStatus, Long bookingId) {
    return null;
  }

  @Override
  public List<Booking> getHostBookings() {
    return List.of();
  }
}
