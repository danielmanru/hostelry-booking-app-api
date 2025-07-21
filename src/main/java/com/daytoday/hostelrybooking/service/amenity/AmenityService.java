package com.daytoday.hostelrybooking.service.amenity;

import com.daytoday.hostelrybooking.dto.AmenityDto;
import com.daytoday.hostelrybooking.exeptions.AlreadyExistsException;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Amenity;
import com.daytoday.hostelrybooking.repository.AmenityRepository;
import com.daytoday.hostelrybooking.request.AddAmenityRequest;
import com.daytoday.hostelrybooking.request.UpdateAmenityRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmenityService implements IAmenityService {
  private final AmenityRepository amenityRepository;
  private final ModelMapper modelMapper;

  @Override
  public List<Amenity> getAllAmenities() {
    return amenityRepository.findAll();
  }

  @Override
  public List<Amenity> getAmenityByCategory(String category) {
    return amenityRepository.findByCategory(category);
  }

  @Override
  public Amenity addAmenity(AddAmenityRequest request) {
    return Optional.of(request).filter(item -> !amenityRepository.existsByName(item.getName()))
        .map(item -> amenityRepository.save(createAmenity(item)))
        .orElseThrow(() -> new AlreadyExistsException(request.getName() + " is already exists"));
  }

  private Amenity createAmenity(AddAmenityRequest request) {
    return new Amenity(
        request.getCategory(),
        request.getName()
    );
  }

  @Override
  public Amenity updateAmenity(UpdateAmenityRequest request, UUID roomId) {
    return amenityRepository.findById(roomId)
        .map(existingAmenity -> updateExistingAmenity(existingAmenity, request))
        .map(amenityRepository::save)
        .orElseThrow(() -> new ResourceNotFoundException("Amenity not found"));
  }

  private Amenity updateExistingAmenity(Amenity existingAmenity, UpdateAmenityRequest request) {
    existingAmenity.setCategory(request.getCategory());
    existingAmenity.setName(request.getName());
    return existingAmenity;
  }

  @Override
  public void deleteAmenity(UUID amenityId) {
    amenityRepository.findById(amenityId)
        .ifPresentOrElse(amenityRepository::delete, () -> {
          throw new ResourceNotFoundException("Amenity not found");
        });
  }

  @Override
  public List<AmenityDto> getConvertedAmenity(List<Amenity> amenities) {
    return amenities.stream().map(this::convertDto).toList();
  }


  @Override
  public AmenityDto convertDto(Amenity amenity) {
    return modelMapper.map(amenity, AmenityDto.class);
  }
}
