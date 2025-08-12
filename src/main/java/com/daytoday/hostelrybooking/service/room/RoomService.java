package com.daytoday.hostelrybooking.service.room;

import com.daytoday.hostelrybooking.dto.ImageDto;
import com.daytoday.hostelrybooking.dto.RoomDto;
import com.daytoday.hostelrybooking.enums.BedTypeEnum;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Amenity;
import com.daytoday.hostelrybooking.model.Image;
import com.daytoday.hostelrybooking.model.Room;
import com.daytoday.hostelrybooking.repository.AmenityRepository;
import com.daytoday.hostelrybooking.repository.ImageRepository;
import com.daytoday.hostelrybooking.repository.PropertyRepository;
import com.daytoday.hostelrybooking.repository.RoomRepository;
import com.daytoday.hostelrybooking.request.AddRoomRequest;

import com.daytoday.hostelrybooking.request.UpdateRoomRequest;
import com.daytoday.hostelrybooking.specification.RoomSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
  private final PropertyRepository propertyRepository;
  private final AmenityRepository amenityRepository;
  private final RoomRepository roomRepository;
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;

  @Override
  public Room addRoom(AddRoomRequest request, UUID propertyId) {
    propertyRepository.findById(propertyId).ifPresentOrElse(request::setProperty, () -> {
      throw new ResourceNotFoundException("Property with id " + propertyId + " not found!");
    });

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
        BedTypeEnum.valueOf(request.getBedType1()),
        BedTypeEnum.valueOf(request.getBedType2()),
        BedTypeEnum.valueOf(request.getBedType3()),
        request.getUnitAvailable(),
        request.getRoomSize(),
        amenities
    );
  }

  @Override
  public List<Room> getRoomByPropertyId(UUID propertyId) {
    return roomRepository.findByPropertyId(propertyId);
  }

  @Override
  public Room getRoomById(UUID roomId) {
    return roomRepository.findById(roomId)
        .orElseThrow(() -> new ResourceNotFoundException("Room not found!"));
  }

  @Override
  public List<Room> searchRoom(int guestCount, int roomCount) {
    Specification<Room> spec = RoomSpecification.hasRoomWithMinGuestAndUnits(guestCount, roomCount);
    return roomRepository.findAll(spec);
  }

  @Override
  public Room updateRoom(UpdateRoomRequest request, UUID roomId) {
    List<Amenity> amenities = amenityRepository.findAllById(request.getAmenityIds());
    return roomRepository.findById(roomId)
        .map(existingRoom -> updateExistingRoom(existingRoom, request, amenities))
        .map(roomRepository::save)
        .orElseThrow(() -> new ResourceNotFoundException("Room with id " + roomId + " not found!"));
  }

  private Room updateExistingRoom(Room existingRoom, UpdateRoomRequest request, List<Amenity> amenities) {
    existingRoom.setRoomName(request.getRoomName());
    existingRoom.setDescription(request.getDescription());
    existingRoom.setMaxGuest(request.getMaxGuest());
    existingRoom.setPricePerNight(request.getPricePerNight());
    existingRoom.setBedType1(request.getBedType1());
    existingRoom.setBedType2(request.getBedType2());
    existingRoom.setBedType3(request.getBedType3());
    existingRoom.setUnitAvailable(request.getUnitAvailable());
    existingRoom.setRoomSize(request.getRoomSize());
    existingRoom.setAmenities(amenities);

    return existingRoom;
  }

  @Override
  public void deleteRoomById(UUID roomId) {
    roomRepository.findById(roomId).ifPresentOrElse(roomRepository::delete, () -> {
      throw new ResourceNotFoundException("Room not found!");
    });
  }

  @Override
  public List<RoomDto> getConvertedRooms(List<Room> rooms) {
    return rooms.stream().map(this::convertToDto).toList();
  }

  @Override
  public RoomDto convertToDto(Room room) {
    RoomDto roomDto = modelMapper.map(room, RoomDto.class);
    List<Image> images = imageRepository.findByRoomId(room.getId());
    List<ImageDto> imageDtos = images.stream()
        .map(image -> modelMapper.map(image, ImageDto.class))
        .toList();
    roomDto.setImages(imageDtos);
    return roomDto;
  }
}
