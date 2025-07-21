package com.daytoday.hostelrybooking.service.booking;

import com.daytoday.hostelrybooking.model.Booking;
import com.daytoday.hostelrybooking.request.CreateBookingRequest;

import java.util.List;

public interface IBookingService {
  Booking createBooking(CreateBookingRequest request);
  List<Booking> getUserBookings();
  Booking getBookingById(Long bookingId);
  Booking updateBookingStatus(String bookingStatus, Long bookingId);
  List<Booking> getHostBookings();
}
