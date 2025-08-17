package com.daytoday.hostelrybooking.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReviewDto {
  private UUID id;
  private UUID propertyId;
  private int rating;
  private String comment;
}
