package com.daytoday.hostelrybooking.service.payment;

import com.daytoday.hostelrybooking.dto.BookingDto;
import com.daytoday.hostelrybooking.dto.PaymentDto;
import com.daytoday.hostelrybooking.enums.PaymentStatusEnum;
import com.daytoday.hostelrybooking.model.Booking;
import com.daytoday.hostelrybooking.model.Payment;
import com.daytoday.hostelrybooking.request.AddPaymentReceiptRequest;
import com.daytoday.hostelrybooking.request.AddPaymentRequest;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

public interface IPaymentService {
  List<Payment> getAllPayment();
  List<Payment> getPaymentByRoom(UUID roomId);
  Payment addPayment(AddPaymentRequest request);
  void updatePaymentStatus(@Nullable AddPaymentReceiptRequest request, UUID paymentId, PaymentStatusEnum paymentStatus);
  List<PaymentDto> getConvertedPayments(List<Payment> payments);
  PaymentDto convertToDto(Payment payment);

}
