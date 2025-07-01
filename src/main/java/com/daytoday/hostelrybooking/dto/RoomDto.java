package com.daytoday.hostelrybooking.dto;

import com.daytoday.hostelrybooking.enums.BedTypeEnum;
import com.daytoday.hostelrybooking.model.Amenity;
import com.daytoday.hostelrybooking.model.Image;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RoomDto {
    private String roomName;
    private String description;
    private int maxGuest;
    private BigDecimal pricePerNight;
    private BedTypeEnum bedType;
    private int unitAvailable;
    private Double roomSize;
    private List<ImageDto> images;
    private List<AmenityDto> amenityIds;
}
