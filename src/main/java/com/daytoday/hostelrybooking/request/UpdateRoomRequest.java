package com.daytoday.hostelrybooking.request;

import com.daytoday.hostelrybooking.enums.BedTypeEnum;
import com.daytoday.hostelrybooking.model.Property;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateRoomRequest {
    private UUID id;
    private String roomName;
    private String description;
    private Short maxGuest;
    private BigDecimal pricePerNight;
    private BedTypeEnum bedType1;
    private BedTypeEnum bedType2;
    private BedTypeEnum bedType3;
    private Short unitAvailable;
    private Double roomSize;
    private List<UUID> amenityIds;
}
