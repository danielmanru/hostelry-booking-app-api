package com.daytoday.hostelrybooking.request;

import lombok.Data;

@Data
public class AddAmenityRequest {
    private Long id;
    private String category;
    private String name;
}
