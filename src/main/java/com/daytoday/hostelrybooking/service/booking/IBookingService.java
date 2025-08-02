package com.daytoday.hostelrybooking.service.booking;

import com.daytoday.hostelrybooking.dto.BookingDto;
import com.daytoday.hostelrybooking.enums.BookingStatusEnum;
import com.daytoday.hostelrybooking.model.Booking;
import com.daytoday.hostelrybooking.request.AddBookingRequest;

import java.util.List;
import java.util.UUID;

public interface IBookingService {
  Booking addBooking(AddBookingRequest request, UUID roomId);
  List<Booking> getUserBookings();
  Booking getBookingById(UUID bookingId);
  Booking updateBookingStatus(BookingStatusEnum bookingStatus, UUID bookingId);
  List<Booking> getBookingsByRoom(UUID roomId);
  List<BookingDto> getConvertedBookings(List<Booking> bookings);
  BookingDto convertToDto(Booking booking);
}
