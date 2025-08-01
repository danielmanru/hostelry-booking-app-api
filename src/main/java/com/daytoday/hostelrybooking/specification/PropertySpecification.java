package com.daytoday.hostelrybooking.specification;

import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.model.Room;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class PropertySpecification {
  public static Specification<Property> hasCity(String city) {
    return (root, query, builder) -> builder.equal(builder.lower(root.get("city")), city.toLowerCase());
  }

  public static Specification<Property> hasRoomWithMinGuestAndUnits(int guestCount, int roomCount) {
    return (root, query, builder) -> {
      Join<Property, Room> roomJoin = root.join("rooms", JoinType.INNER);

      int requiredPerRoomCapacity = (int) Math.ceil((double) guestCount / roomCount);
      Subquery<Double> subquery = Objects.requireNonNull(query).subquery(Double.class);
      Root<Room> room = subquery.from(Room.class);
      subquery.select(builder.min(room.get("price")));

      Predicate sameProperty = builder.equal(room.get("property"), root);
      Predicate guestCheck = builder.greaterThanOrEqualTo(room.get("maxGuest"), requiredPerRoomCapacity);
      Predicate unitCheck = builder.greaterThanOrEqualTo(room.get("unitAvailable"), roomCount);

      subquery.where(sameProperty, guestCheck, unitCheck);

      Predicate guestCond = builder.greaterThanOrEqualTo(roomJoin.get("maxGuest"), requiredPerRoomCapacity);
      Predicate unitCond = builder.greaterThanOrEqualTo(roomJoin.get("unitAvailable"), roomCount);
      Predicate priceMatch = builder.equal(roomJoin.get("price"), subquery);

      return builder.and(guestCond, unitCond, priceMatch);
    };
  }

  public static Specification<Property> orderByLowestPrice() {
    return (root, query, builder) -> {
      Join<Property, Room> roomJoin = root.join("rooms", JoinType.INNER);
      Objects.requireNonNull(query).groupBy(root.get("id"));
      query.orderBy(builder.asc(builder.min(roomJoin.get("price"))));
      return null;
    };
  }

  public static Specification<Property> orderByHighestPrice() {
    return (root, query, builder) -> {
      Join<Property, Room> roomJoin = root.join("rooms", JoinType.INNER);
      Objects.requireNonNull(query).groupBy(root.get("id"));
      query.orderBy(builder.desc(builder.min(roomJoin.get("price"))));
      return null;
    };
  }

  public static Specification<Property> orderByHighestRating() {
    return (root, query, builder) -> {
      Objects.requireNonNull(query).orderBy(builder.desc(root.get("rating")));
      return null;
    };
  }
}
