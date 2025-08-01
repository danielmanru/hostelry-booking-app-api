package com.daytoday.hostelrybooking.specification;

import com.daytoday.hostelrybooking.model.Room;
import org.springframework.data.jpa.domain.Specification;

public class RoomSpecification {
  public static Specification<Room> hasRoomWithMinGuestAndUnits(int guestCount, int roomCount){
    return (root, query, builder) -> {
      int requiredPerRoomCapacity = (int) Math.ceil((double) guestCount / roomCount);
      return builder.and(
          builder.greaterThanOrEqualTo(root.get("maxGuest"), requiredPerRoomCapacity),
          builder.greaterThanOrEqualTo(root.get("unitAvailable"), roomCount)
      );
    };
  }
}
