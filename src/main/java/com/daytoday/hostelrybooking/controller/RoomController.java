package com.daytoday.hostelrybooking.controller;

import com.daytoday.hostelrybooking.dto.RoomDto;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Room;
import com.daytoday.hostelrybooking.request.AddRoomRequest;
import com.daytoday.hostelrybooking.request.UpdateRoomRequest;
import com.daytoday.hostelrybooking.response.ApiResponse;
import com.daytoday.hostelrybooking.service.room.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/rooms")
public class RoomController {
    private IRoomService roomService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addRoom(@RequestBody AddRoomRequest request, @PathVariable Long propertyId) {
        try {
            Room room = roomService.addRoom(request, propertyId);
            RoomDto roomDto = roomService.convertToDto(room);
            return ResponseEntity.ok(new ApiResponse("Success add room to property " + propertyId, roomDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/property/{propertyId}/rooms")
    public ResponseEntity<ApiResponse> getRoomByPropertyId(@PathVariable Long propertyId) {
        try {
            List<Room> rooms = roomService.getRoomByPropertyId(propertyId);
            if (rooms.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Rooms with property id " + propertyId + " not found!", null));
            }
            List<RoomDto> convertedRooms = roomService.getConvertedRooms(rooms);
            return ResponseEntity.ok(new ApiResponse("Success", convertedRooms));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/room/{roomId}/room")
    public ResponseEntity<ApiResponse> getRoomById(@PathVariable Long roomId) {
        try {
            Room room = roomService.getRoomById(roomId);
            RoomDto roomDto = roomService.convertToDto(room);
            return ResponseEntity.ok(new ApiResponse("Success", roomDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/room/{roomId)")
    public ResponseEntity<ApiResponse> updateRoom(@RequestBody UpdateRoomRequest request, @PathVariable Long roomId) {
        try {
            Room room = roomService.updateRoom(request, roomId);
            RoomDto roomDto = roomService.convertToDto(room);
            return ResponseEntity.ok(new ApiResponse("Success update room " + roomId, roomDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<ApiResponse> deleteRoomById(@PathVariable Long roomId) {
        try {
            roomService.deleteRoomById(roomId);
            return ResponseEntity.ok(new ApiResponse("Success Delete room " + roomId, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
