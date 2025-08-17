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
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private int rating;
    private String comment;

    public Review(User user, Booking booking, Property property, int rating, String comment) {
        this.user = user;
        this.booking = booking;
        this.property = property;
        this.rating = rating;
        this.comment = comment;
    }
}
