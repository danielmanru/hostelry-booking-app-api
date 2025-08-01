package com.daytoday.hostelrybooking.request;

import com.daytoday.hostelrybooking.enums.BedTypeEnum;
import com.daytoday.hostelrybooking.model.Amenity;
import com.daytoday.hostelrybooking.model.Property;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class AddRoomRequest {
    private UUID id;
    private Property property;
    private String roomName;
    private String description;
    private Short maxGuest;
    private BigDecimal pricePerNight;
    private String bedType;
    private Short unitAvailable;
    private Double roomSize;
    private List<UUID> amenityIds;
}
