package com.daytoday.hostelrybooking.model;

import com.daytoday.hostelrybooking.enums.BedTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Room extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private String roomName;
    private String description;
    private int maxGuest;

    private BigDecimal pricePerNight;

    @Enumerated(EnumType.STRING)
    private BedTypeEnum bedType;

    private int unitAvailable;

    private Double roomSize;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @ManyToMany
    @JoinTable(name = "room_amenities",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    private List<Amenity> amenities;

    public Room(Property property, String roomName, String description, int maxGuest, BigDecimal pricePerNight, BedTypeEnum bedType, int unitAvailable, Double roomSize, List<Amenity> amenities) {
        this.property = property;
        this.roomName = roomName;
        this.description = description;
        this.maxGuest = maxGuest;
        this.pricePerNight = pricePerNight;
        this.bedType = bedType;
        this.unitAvailable = unitAvailable;
        this.roomSize = roomSize;
        this.amenities = amenities;
    }
}
