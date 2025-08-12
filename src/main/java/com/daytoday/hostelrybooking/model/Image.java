package com.daytoday.hostelrybooking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private String filePublicId;
    private String fileName;
    private String imageUrl;
}
