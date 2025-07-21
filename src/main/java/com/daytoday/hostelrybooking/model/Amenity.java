package com.daytoday.hostelrybooking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Amenity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String category;
    private String name;

    @ManyToMany(mappedBy = "amenities")
    private List<Room> rooms;

    public Amenity(String category, String name) {
        this.category = category;
        this.name = name;
    }
}
