package com.daytoday.hostelrybooking.service.room;

import com.daytoday.hostelrybooking.dto.RoomDto;
import com.daytoday.hostelrybooking.model.Room;
import com.daytoday.hostelrybooking.request.AddRoomRequest;
import com.daytoday.hostelrybooking.request.UpdateRoomRequest;

import java.util.List;
import java.util.UUID;

public interface IRoomService {
    Room addRoom(AddRoomRequest request, UUID propertyId);
    List<Room> getRoomByPropertyId(UUID propertyId);
    Room getRoomById(UUID roomId);
    Room updateRoom(UpdateRoomRequest request, UUID roomId);
    void deleteRoomById(UUID roomId);
    List<RoomDto> getConvertedRooms(List<Room> rooms);
    RoomDto convertToDto(Room room);
}
