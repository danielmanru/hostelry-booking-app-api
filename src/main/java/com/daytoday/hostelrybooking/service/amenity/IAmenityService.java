package com.daytoday.hostelrybooking.service.amenity;

import com.daytoday.hostelrybooking.dto.AmenityDto;
import com.daytoday.hostelrybooking.model.Amenity;
import com.daytoday.hostelrybooking.request.AddAmenityRequest;
import com.daytoday.hostelrybooking.request.UpdateAmenityRequest;

import java.util.List;
import java.util.UUID;

public interface IAmenityService {
  List<Amenity> getAllAmenities();

  List<Amenity> getAmenityByCategory(String category);

  Amenity addAmenity(AddAmenityRequest amenity);

  Amenity updateAmenity(UpdateAmenityRequest request, UUID roomId);

  void deleteAmenity(UUID amenityId);

  List<AmenityDto> getConvertedAmenity(List<Amenity> amenities);

  AmenityDto convertDto(Amenity amenity);
}
