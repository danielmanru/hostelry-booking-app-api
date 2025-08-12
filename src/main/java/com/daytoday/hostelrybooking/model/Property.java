package com.daytoday.hostelrybooking.model;

import com.daytoday.hostelrybooking.enums.PropertyTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Property extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    private String name;
    private String description;
    private String address;
    private String city;
    private String country;

    @Enumerated(EnumType.STRING)
    private PropertyTypeEnum type;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
    private Double rating;
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;


    public Property(User owner, String name, String description, String address, String city, String country, PropertyTypeEnum type) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.country = country;
        this.type = type;
    }

    public void calculateRating() {
        this.rating = this.reviews.stream()
            .mapToInt(Review :: getRating)
            .average()
            .orElse(0.0);
    }
}
