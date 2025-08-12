package com.daytoday.hostelrybooking.service.booking;

import com.daytoday.hostelrybooking.dto.BookingDto;
import com.daytoday.hostelrybooking.enums.BookingStatusEnum;
import com.daytoday.hostelrybooking.enums.UserRoleEnum;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Booking;
import com.daytoday.hostelrybooking.model.Room;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.repository.BookingRepository;
import com.daytoday.hostelrybooking.repository.RoomRepository;
import com.daytoday.hostelrybooking.request.AddBookingRequest;
import com.daytoday.hostelrybooking.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
  private final RoomRepository roomRepository;
  private final IUserService userService;
  private final BookingRepository bookingRepository;
  private final ModelMapper modelMapper;

  @Override
  public Booking addBooking(AddBookingRequest request, UUID roomId) {
    User user = userService.getAuthenticatedUser();
    Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));

    BigDecimal totalAmount = new BigDecimal(request.getRoomCount())
        .multiply(room.getPricePerNight())
        .multiply(new BigDecimal(request.getNightCount()));

    Booking booking = bookingRepository.save(createBooking(request, user, room, totalAmount));
    room.decreaseUnitAvailable(request.getRoomCount());
    roomRepository.save(room);
    return booking;
  }

  private Booking createBooking(AddBookingRequest request, User user, Room room, BigDecimal totalAmount) {
    return new Booking(
        user,
        room,
        request.getRoomCount(),
        request.getCheckInDate(),
        request.getCheckOutDate(),
        request.getIsForMe(),
        request.getGuestCount(),
        request.getGuestName(),
        totalAmount,
        BookingStatusEnum.valueOf("INITIATED")
    );
  }

  @Override
  public List<Booking> getUserBookings() {
    User user = userService.getAuthenticatedUser();
    return bookingRepository.findByUser(user);
  }

  @Override
  public Booking getBookingById(UUID bookingId) {
    User user = userService.getAuthenticatedUser();
    Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    if (user.getRole().equals(UserRoleEnum.valueOf("ROLE_USER")) && !booking.getUser().equals(user)) {
      throw new AccessDeniedException("Unauthorized");
    } else if (user.getRole().equals(UserRoleEnum.valueOf("ROLE_OWNER"))) {
      User owner = booking.getRoom().getProperty().getOwner();
      if(!user.equals(owner)) {
        throw new AccessDeniedException("Unauthorized");
      }
    }
    return booking;
  }

  @Override
  public Booking updateBookingStatus(BookingStatusEnum bookingStatus, UUID bookingId) {
    User user = userService.getAuthenticatedUser();
    Booking booking = getBookingById(bookingId);
    if (user.getRole().equals(UserRoleEnum.valueOf("ROLE_ADMIN"))) {
      booking.setStatus(bookingStatus);
      if (bookingStatus == BookingStatusEnum.COMPLETED) {
        Room room = booking.getRoom();
        room.increaseUnitAvailable(booking.getRoomCount());
        roomRepository.save(room);
      }
    } else if (user.getRole().equals(UserRoleEnum.valueOf("ROLE_USER"))) {
      if (!booking.getUser().equals(user)) {
        throw new AccessDeniedException("Unauthorized");
      }
      if (bookingStatus == BookingStatusEnum.CANCELLED) {
        booking.setStatus(bookingStatus);
      } else {
        throw new AccessDeniedException("Unauthorized");
      }
    } else if (user.getRole().equals(UserRoleEnum.valueOf("ROLE_OWNER"))) {
      if (bookingStatus == BookingStatusEnum.CHECKED_IN) {
        booking.setStatus(bookingStatus);
      } else {
        throw new AccessDeniedException("Unauthorized");
      }
    } else {
      throw new AccessDeniedException("Unauthorized");
    }
    return bookingRepository.save(booking);
  }

  @Override
  public List<Booking> getBookingsByRoom(UUID roomId) {
    return bookingRepository.findByRoomId(roomId);
  }

  @Override
  public List<BookingDto> getConvertedBookings(List<Booking> bookings) {
    return bookings.stream().map(this :: convertToDto).toList();
  }

  @Override
  public BookingDto convertToDto(Booking booking) {
    return modelMapper.map(booking, BookingDto.class);
  }
}
