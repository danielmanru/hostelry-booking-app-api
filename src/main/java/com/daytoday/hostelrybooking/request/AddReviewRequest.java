package com.daytoday.hostelrybooking.request;

import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.model.User;
import lombok.Data;

@Data
public class AddReviewRequest {
  private int rating;
  private String comment;
}
