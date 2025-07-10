package com.daytoday.hostelrybooking.request;

import lombok.Data;

@Data
public class UpdateAmenityRequest {
    private Long id;
    private String category;
    private String name;
}
