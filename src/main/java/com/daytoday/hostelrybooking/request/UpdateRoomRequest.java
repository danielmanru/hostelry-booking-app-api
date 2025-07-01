package com.daytoday.hostelrybooking.request;

import com.daytoday.hostelrybooking.enums.BedTypeEnum;
import com.daytoday.hostelrybooking.model.Property;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateRoomRequest {
    private Long id;
    private String roomName;
    private String description;
    private int maxGuest;
    private BigDecimal pricePerNight;
    private BedTypeEnum bedType;
    private int unitAvailable;
    private Double roomSize;
    private List<Long> amenityIds;
}
