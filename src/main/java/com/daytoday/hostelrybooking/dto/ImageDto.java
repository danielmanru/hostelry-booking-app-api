package com.daytoday.hostelrybooking.dto;

import lombok.Data;

@Data
public class ImageDto {
    private Long id;
    private String filePublicId;
    private String fileName;
    private String imageUrl;
}
