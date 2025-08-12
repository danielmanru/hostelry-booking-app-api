package com.daytoday.hostelrybooking.dto;

import com.daytoday.hostelrybooking.enums.BedTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class RoomDto {
    private UUID id;
    private String roomName;
    private String description;
    private int maxGuest;
    private BigDecimal pricePerNight;
    private BedTypeEnum bedType1;
    private BedTypeEnum bedType2;
    private BedTypeEnum bedType3;
    private int unitAvailable;
    private Double roomSize;
    private List<ImageDto> images;
    private List<AmenityDto> amenities;
}
