package com.daytoday.hostelrybooking.request;

import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.model.User;
import lombok.Data;

@Data
public class AddReviewRequest {
  private User user;
  private Property property;
  private int rating;
  private String comment;
}
