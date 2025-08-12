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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/rooms")
public class RoomController {
    private final IRoomService roomService;

    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addRoom(@RequestBody AddRoomRequest request, @RequestParam UUID propertyId) {
        try {
            Room room = roomService.addRoom(request, propertyId);
            RoomDto roomDto = roomService.convertToDto(room);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Success add room to property " + propertyId, roomDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<ApiResponse> getRoomByPropertyId(@PathVariable UUID propertyId) {
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

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse> getRoomById(@PathVariable UUID roomId) {
        try {
            Room room = roomService.getRoomById(roomId);
            RoomDto roomDto = roomService.convertToDto(room);
            return ResponseEntity.ok(new ApiResponse("Success", roomDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchRoom(@RequestParam int guestCount, @RequestParam int roomCount) {
        try {
            List<Room> rooms = roomService.searchRoom(guestCount, roomCount);
            if (rooms.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Room not found!", null));
            }
            List<RoomDto> roomDtos = roomService.getConvertedRooms(rooms);
            return ResponseEntity.ok(new ApiResponse("Success", roomDtos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN')")
    @PutMapping("/update/room/{roomId}")
    public ResponseEntity<ApiResponse> updateRoom(@RequestBody UpdateRoomRequest request, @PathVariable UUID roomId) {
        try {
            Room room = roomService.updateRoom(request, roomId);
            RoomDto roomDto = roomService.convertToDto(room);
            return ResponseEntity.ok(new ApiResponse("Success update room " + roomId, roomDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN')")
    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<ApiResponse> deleteRoomById(@PathVariable UUID roomId) {
        try {
            roomService.deleteRoomById(roomId);
            return ResponseEntity.ok(new ApiResponse("Success Delete room " + roomId, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
