package com.daytoday.hostelrybooking.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AddAmenityRequest {
  private UUID id;
  private String category;
  private String name;
}
