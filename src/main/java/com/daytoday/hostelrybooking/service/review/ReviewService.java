package com.daytoday.hostelrybooking.service.review;

import com.daytoday.hostelrybooking.dto.PaymentDto;
import com.daytoday.hostelrybooking.dto.ReviewDto;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.model.Review;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.repository.PropertyRepository;
import com.daytoday.hostelrybooking.repository.ReviewRepository;
import com.daytoday.hostelrybooking.request.AddReviewRequest;
import com.daytoday.hostelrybooking.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
  private final ReviewRepository reviewRepository;
  private final IUserService userService;
  private final ModelMapper modelMapper;
  private final PropertyRepository propertyRepository;

  @Override
  public List<Review> getReviewByProperty(UUID propertyId) {
    return reviewRepository.findByPropertyId(propertyId);
  }

  @Override
  public Review addReview(AddReviewRequest request, UUID propertyId) {
    User user = userService.getAuthenticatedUser();
    Property property = propertyRepository.findById(propertyId)
        .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
    Review savedReview = reviewRepository.save(createReview(request, property, user));
    Property savedReviewProperty = savedReview.getProperty();
    savedReviewProperty.calculateRating();
    propertyRepository.save(savedReviewProperty);
    return savedReview;
  }

  private Review createReview(AddReviewRequest request, Property property, User user) {
    return new Review(
        user,
        property,
        request.getRating(),
        request.getComment()
    );
  }

  @Override
  public Review getReviewById(UUID reviewId) {
    return reviewRepository.findById(reviewId)
        .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
  }

  @Override
  public List<Review> getUserReview() {
    User user = userService.getAuthenticatedUser();
    return reviewRepository.findByUser(user);
  }

  @Override
  public void deleteReview(UUID reviewId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    reviewRepository.delete(review);
    Property property = propertyRepository.findById(review.getProperty().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
    property.calculateRating();
    propertyRepository.save(property);
  }

  @Override
  public List<ReviewDto> getConvertedReviews(List<Review> reviews) {
    return reviews.stream().map(this::convertToDto).toList();
  }

  @Override
  public ReviewDto convertToDto(Review review) {
    return modelMapper.map(review, ReviewDto.class);
  }
}
