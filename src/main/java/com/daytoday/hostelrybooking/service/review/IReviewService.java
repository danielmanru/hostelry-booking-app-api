package com.daytoday.hostelrybooking.service.review;

import com.daytoday.hostelrybooking.dto.PaymentDto;
import com.daytoday.hostelrybooking.dto.ReviewDto;
import com.daytoday.hostelrybooking.model.Payment;
import com.daytoday.hostelrybooking.model.Review;
import com.daytoday.hostelrybooking.request.AddReviewRequest;

import java.util.List;
import java.util.UUID;

public interface IReviewService {
  List<Review> getReviewByProperty(UUID propertyId);
  Review addReview(AddReviewRequest request, UUID bookingId);
  Review getReviewById(UUID reviewId);
  List<Review> getUserReview();
  void deleteReview(UUID reviewId);
  List<ReviewDto> getConvertedReviews(List<Review> reviews);
  ReviewDto convertToDto(Review review);
}
