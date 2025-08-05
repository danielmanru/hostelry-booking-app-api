package com.daytoday.hostelrybooking.controller;

import com.daytoday.hostelrybooking.dto.BookingDto;
import com.daytoday.hostelrybooking.dto.PaymentDto;
import com.daytoday.hostelrybooking.enums.PaymentStatusEnum;
import com.daytoday.hostelrybooking.exeptions.BadRequestException;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.exeptions.StatusCannotBeChangedException;
import com.daytoday.hostelrybooking.model.Booking;
import com.daytoday.hostelrybooking.model.Payment;
import com.daytoday.hostelrybooking.request.AddBookingRequest;
import com.daytoday.hostelrybooking.request.AddPaymentReceiptRequest;
import com.daytoday.hostelrybooking.request.AddPaymentRequest;
import com.daytoday.hostelrybooking.response.ApiResponse;
import com.daytoday.hostelrybooking.service.payment.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/payments")
public class PaymentController {
  private final IPaymentService paymentService;

  @PreAuthorize("hasRole('ROLE_USER')")
  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addPayment(@RequestBody AddPaymentRequest request, @RequestParam UUID bookingId) {
    try {
      Payment payment = paymentService.addPayment(request, bookingId);
      PaymentDto paymentDto = paymentService.convertToDto(payment);
      return ResponseEntity.status(CREATED).body(new ApiResponse("Success", paymentDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/")
  public ResponseEntity<ApiResponse> getAllPayments() {
    try {
      List<Payment> payments = paymentService.getAllPayment();
      if (payments.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Payment not found", null));
      }
      List<PaymentDto> paymentDtos = paymentService.getConvertedPayments(payments);
      return ResponseEntity.ok(new ApiResponse("Success", paymentDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/room/{roomId}")
  public ResponseEntity<ApiResponse> getPaymentByRoom(@PathVariable UUID roomId) {
    try {
      List<Payment> payments = paymentService.getPaymentByRoom(roomId);
      if (payments.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Payment not found", null));
      }
      List<PaymentDto> paymentDtos = paymentService.getConvertedPayments(payments);
      return ResponseEntity.ok(new ApiResponse("Success", paymentDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/update/status")
  public ResponseEntity<ApiResponse> updatePaymentStatus(@Nullable @RequestBody AddPaymentReceiptRequest request, @RequestParam UUID paymentId, @RequestParam String paymentStatus) {
    try {
      paymentService.updatePaymentStatus(request, paymentId, PaymentStatusEnum.valueOf(paymentStatus));
      return ResponseEntity.ok(new ApiResponse("Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    } catch (StatusCannotBeChangedException e) {
      return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
    } catch (BadRequestException e) {
      return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
