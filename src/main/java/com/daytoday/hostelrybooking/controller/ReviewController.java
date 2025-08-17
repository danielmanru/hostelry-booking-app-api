package com.daytoday.hostelrybooking.controller;

import com.daytoday.hostelrybooking.dto.PaymentDto;
import com.daytoday.hostelrybooking.dto.ReviewDto;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Payment;
import com.daytoday.hostelrybooking.model.Review;
import com.daytoday.hostelrybooking.request.AddPaymentRequest;
import com.daytoday.hostelrybooking.request.AddReviewRequest;
import com.daytoday.hostelrybooking.response.ApiResponse;
import com.daytoday.hostelrybooking.service.review.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/reviews")
public class ReviewController {
  private final IReviewService reviewService;

  @PreAuthorize("hasRole('ROLE_USER')")
  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addReview(@RequestBody AddReviewRequest request, @RequestParam UUID bookingId) {
    try {
      Review review = reviewService.addReview(request, bookingId);
      ReviewDto reviewDto = reviewService.convertToDto(review);
      return ResponseEntity.status(CREATED).body(new ApiResponse("Success", reviewDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }


  @GetMapping("/property/{propertyId}")
  public ResponseEntity<ApiResponse> getReviewByProperty(@PathVariable UUID propertyId) {
    try {
      List<Review> reviews = reviewService.getReviewByProperty(propertyId);
      if(reviews.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Review not found", null));
      }
      List<ReviewDto> reviewDtos = reviewService.getConvertedReviews(reviews);
      return ResponseEntity.ok(new ApiResponse("Success", reviewDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/user/me/reviews")
  public ResponseEntity<ApiResponse> getUserReview() {
    try {
      List<Review> reviews = reviewService.getUserReview();
      if(reviews.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Review not found", null));
      }
      List<ReviewDto> reviewDtos = reviewService.getConvertedReviews(reviews);
      return ResponseEntity.ok(new ApiResponse("Success", reviewDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/review/{reviewId}")
  public ResponseEntity<ApiResponse> getReviewById(@PathVariable UUID reviewId) {
    try {
      Review review = reviewService.getReviewById(reviewId);
      ReviewDto reviewDto = reviewService.convertToDto(review);
      return ResponseEntity.ok(new ApiResponse("Success", reviewDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/delete")
  public ResponseEntity<ApiResponse> deleteReview(@RequestParam UUID reviewId) {
    try {
      reviewService.deleteReview(reviewId);
      return ResponseEntity.ok(new ApiResponse("Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
