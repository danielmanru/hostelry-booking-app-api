package com.daytoday.hostelrybooking.request;

import com.daytoday.hostelrybooking.enums.PropertyTypeEnum;
import com.daytoday.hostelrybooking.model.User;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdatePropertyRequest {
  private UUID id;
  private String name;
  private String description;
  private String address;
  private String city;
  private String country;
  private PropertyTypeEnum type;
}
