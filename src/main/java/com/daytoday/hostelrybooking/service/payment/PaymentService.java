package com.daytoday.hostelrybooking.service.payment;

import com.daytoday.hostelrybooking.dto.PaymentDto;
import com.daytoday.hostelrybooking.enums.PaymentMethodEnum;
import com.daytoday.hostelrybooking.enums.PaymentStatusEnum;
import com.daytoday.hostelrybooking.exeptions.AlreadyExistsException;
import com.daytoday.hostelrybooking.exeptions.BadRequestException;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.exeptions.StatusCannotBeChangedException;
import com.daytoday.hostelrybooking.model.Booking;
import com.daytoday.hostelrybooking.model.Payment;
import com.daytoday.hostelrybooking.model.PaymentReceipt;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.repository.BookingRepository;
import com.daytoday.hostelrybooking.repository.PaymentReceiptRepository;
import com.daytoday.hostelrybooking.repository.PaymentRepository;
import com.daytoday.hostelrybooking.request.AddPaymentReceiptRequest;
import com.daytoday.hostelrybooking.request.AddPaymentRequest;
import com.daytoday.hostelrybooking.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
  private final PaymentRepository paymentRepository;
  private final BookingRepository bookingRepository;
  private final PaymentReceiptRepository paymentReceiptRepository;
  private final ModelMapper modelMapper;
  private final IUserService userService;

  @Override
  public List<Payment> getAllPayment() {
    return paymentRepository.findAll();
  }

  @Override
  public List<Payment> getPaymentByRoom(UUID roomId) {
    return paymentRepository.findByBookingRoomId(roomId);
  }

  @Override
  public Payment addPayment(AddPaymentRequest request, UUID bookingId) {
    User user = userService.getAuthenticatedUser();
    Booking booking = bookingRepository.findById(bookingId)
        .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

    if (!user.equals(booking.getUser())) {
      throw new AccessDeniedException("Forbidden");
    }

    if (booking.getPayment() != null) {
      throw new AlreadyExistsException("Payment is already created");
    }

    if (!booking.getTotalAmount().equals(request.getTotalAmount())) {
      throw new BadRequestException("Total amount is incorrect");
    }
//    Assuming backend receive respond from payment gateway
    HashMap<String, Object> pgResponse = new HashMap<>();
    pgResponse.put("paymentUrl", "https://www.paymentgateway.com/payment/example");
    pgResponse.put("expiresAt", LocalDateTime.now().plusHours(3));

    Payment paymentSaved = paymentRepository.save(createPayment(request, booking, pgResponse));
    booking.setPayment(paymentSaved);
    bookingRepository.save(booking);

    return paymentSaved;
  }

  private Payment createPayment(AddPaymentRequest request, Booking booking, HashMap<String, Object> pgResponse) {
    LocalDateTime expiresAt = (LocalDateTime) pgResponse.get("expiresAt");
    return new Payment(
        booking,
        request.getTotalAmount(),
        PaymentMethodEnum.valueOf(request.getPaymentMethod()),
        pgResponse.get("paymentUrl").toString(),
        expiresAt,
        PaymentStatusEnum.valueOf("UNPAID")
    );
  }

  @Override
  public Payment updatePaymentStatus(@Nullable AddPaymentReceiptRequest request, UUID paymentId, PaymentStatusEnum paymentStatus) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new ResourceNotFoundException("Payment Not Found"));
    if(payment.getStatus() != PaymentStatusEnum.UNPAID) {
      throw new StatusCannotBeChangedException("Payment status cannot be changed anymore");
    }

    if (paymentStatus == PaymentStatusEnum.PAID) {
      if (request == null) {
        throw new BadRequestException("Request cannot be null");
      }

      PaymentReceipt paymentReceipt = new PaymentReceipt();
      paymentReceipt.setPayment(payment);
      paymentReceipt.setTransactionId(request.getTransactionId());
      paymentReceipt.setAmount(request.getAmount());
      paymentReceipt.setPaidAt(request.getPaidAt());
      PaymentReceipt paymentReceiptSaved = paymentReceiptRepository.save(paymentReceipt);

      payment.setStatus(paymentStatus);
      payment.setPaymentReceipt(paymentReceiptSaved);
      return paymentRepository.save(payment);
    }
    payment.setStatus(paymentStatus);
    return paymentRepository.save(payment);
  }

  @Override
  public List<PaymentDto> getConvertedPayments(List<Payment> payments) {
    return payments.stream().map(this :: convertToDto).toList();
  }

  @Override
  public PaymentDto convertToDto(Payment payment) {
    return modelMapper.map(payment, PaymentDto.class);
  }
}
