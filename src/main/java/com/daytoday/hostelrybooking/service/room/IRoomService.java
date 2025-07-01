package com.daytoday.hostelrybooking.service.room;

import com.daytoday.hostelrybooking.dto.RoomDto;
import com.daytoday.hostelrybooking.model.Room;
import com.daytoday.hostelrybooking.request.AddRoomRequest;
import com.daytoday.hostelrybooking.request.UpdateRoomRequest;

import java.util.List;

public interface IRoomService {
    Room addRoom(AddRoomRequest request, Long propertyId);
    List<Room> getRoomByPropertyId(Long propertyId);
    Room getRoomById(Long roomId);
    Room updateRoom(UpdateRoomRequest request, Long roomId);
    void deleteRoomById(Long roomId);
    List<RoomDto> getConvertedRooms(List<Room> rooms);
    RoomDto convertToDto(Room room);
}
