package com.daytoday.hostelrybooking.service.room;

import com.daytoday.hostelrybooking.dto.ImageDto;
import com.daytoday.hostelrybooking.dto.RoomDto;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Amenity;
import com.daytoday.hostelrybooking.model.Image;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.model.Room;
import com.daytoday.hostelrybooking.repository.AmenityRepository;
import com.daytoday.hostelrybooking.repository.PropertyRepository;
import com.daytoday.hostelrybooking.repository.RoomRepository;
import com.daytoday.hostelrybooking.request.AddRoomRequest;

import com.daytoday.hostelrybooking.request.UpdateRoomRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final PropertyRepository propertyRepository;
    private final AmenityRepository amenityRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Override
    public Room addRoom(AddRoomRequest request, Long propertyId) {
        propertyRepository.findById(propertyId).ifPresentOrElse(request :: setProperty, () -> {throw new ResourceNotFoundException("Property with id " + propertyId + " not found!");});

        List<Amenity> amenities = amenityRepository.findAllById(request.getAmenityIds());

        return roomRepository.save(createRoom(request, amenities));
    }

    private Room createRoom(AddRoomRequest request, List<Amenity> amenities) {
        return new Room(
                request.getProperty(),
                request.getRoomName(),
                request.getDescription(),
                request.getMaxGuest(),
                request.getPricePerNight(),
                request.getBedType(),
                request.getUnitAvailable(),
                request.getRoomSize(),
                amenities
        );
    }

    @Override
    public List<Room> getRoomByPropertyId(Long propertyId) {
        return roomRepository.findByPropertyId(propertyId);
    }

    @Override
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found!"));
    }

    @Override
    public Room updateRoom(UpdateRoomRequest request, Long roomId) {
        List<Amenity> amenities = amenityRepository.findAllById(request.getAmenityIds());
        return roomRepository.findById(roomId)
                .map(existingRoom -> updateExistingRoom(existingRoom, request, amenities))
                .map(roomRepository :: save)
                .orElseThrow(() -> new ResourceNotFoundException("Room with id " + roomId + " not found!"));
    }

    private Room updateExistingRoom(Room existingRoom, UpdateRoomRequest request, List<Amenity> amenities) {
        existingRoom.setRoomName(request.getRoomName());
        existingRoom.setDescription(request.getDescription());
        existingRoom.setMaxGuest(request.getMaxGuest());
        existingRoom.setPricePerNight(request.getPricePerNight());
        existingRoom.setBedType(request.getBedType());
        existingRoom.setUnitAvailable(request.getUnitAvailable());
        existingRoom.setRoomSize(request.getRoomSize());
        existingRoom.setAmenities(amenities);

        return existingRoom;
    }

    @Override
    public void deleteRoomById(Long roomId) {
        roomRepository.findById(roomId).ifPresentOrElse(roomRepository :: delete, () -> {
            throw new ResourceNotFoundException("Room not found!");
        });
    }

    @Override
    public List<RoomDto> getConvertedRooms(List<Room> rooms) {
        return rooms.stream().map(this :: convertToDto).toList();
    }

    @Override
    public RoomDto convertToDto(Room room) {
        RoomDto roomDto = modelMapper.map(room, RoomDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        roomDto.setImages(imageDtos);
        return roomDto;
}
