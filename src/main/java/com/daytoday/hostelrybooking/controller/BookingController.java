package com.daytoday.hostelrybooking.controller;

import com.daytoday.hostelrybooking.dto.BookingDto;
import com.daytoday.hostelrybooking.dto.UserDto;
import com.daytoday.hostelrybooking.enums.BookingStatusEnum;
import com.daytoday.hostelrybooking.exeptions.AlreadyExistsException;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Booking;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.request.AddBookingRequest;
import com.daytoday.hostelrybooking.request.UpdateUserRequest;
import com.daytoday.hostelrybooking.response.ApiResponse;
import com.daytoday.hostelrybooking.service.booking.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/bookings")
public class BookingController {
  private final IBookingService bookingService;

  @PreAuthorize("hasRole('ROLE_USER')")
  @PostMapping("/room/{roomId}/add")
  public ResponseEntity<ApiResponse> addBooking(@RequestBody AddBookingRequest request, @PathVariable UUID roomId) {
    try {
      Booking booking = bookingService.addBooking(request, roomId);
      BookingDto bookingDto = bookingService.convertToDto(booking);
      return ResponseEntity.status(CREATED).body(new ApiResponse("Success", bookingDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/user/bookings")
  public ResponseEntity<ApiResponse> getUserBookings() {
    try {
      List<Booking> bookings = bookingService.getUserBookings();
      if(bookings.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Booking not found!", null));
      }
      List<BookingDto> bookingDtos = bookingService.getConvertedBookings(bookings);
      return ResponseEntity.ok(new ApiResponse("Success", bookingDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/booking/{bookingId")
  public ResponseEntity<ApiResponse> createUser(@PathVariable UUID bookingId) {
    try {
      Booking booking = bookingService.getBookingById(bookingId);
      BookingDto bookingDto = bookingService.convertToDto(booking);
      return ResponseEntity.ok(new ApiResponse("Success", bookingDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PatchMapping("/update/status")
  public ResponseEntity<ApiResponse> updateUser(@RequestParam String bookingStatus, @RequestParam UUID bookingId) {
    try {
      Booking booking = bookingService.updateBookingStatus(BookingStatusEnum.valueOf(bookingStatus), bookingId);
      BookingDto bookingDto = bookingService.convertToDto(booking);
      return ResponseEntity.ok(new ApiResponse("Success", bookingDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasAnyRole(['ROLE_ADMIN', 'ROLE_OWNER'])")
  @GetMapping("/room/{roomId")
  public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID roomId) {
    try {
      List<Booking> bookings = bookingService.getBookingsByRoom(roomId);
      if(bookings.isEmpty()){
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Booking not found!", null));
      }
      List<BookingDto> bookingDtos = bookingService.getConvertedBookings(bookings);
      return ResponseEntity.ok(new ApiResponse("Success", bookingDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
