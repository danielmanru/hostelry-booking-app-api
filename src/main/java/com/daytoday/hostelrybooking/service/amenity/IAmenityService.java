package com.daytoday.hostelrybooking.service.amenity;

import com.daytoday.hostelrybooking.dto.AmenityDto;
import com.daytoday.hostelrybooking.model.Amenity;
import com.daytoday.hostelrybooking.request.AddAmenityRequest;
import com.daytoday.hostelrybooking.request.UpdateAmenityRequest;

import java.util.List;

public interface IAmenityService {
    List<Amenity> getAllAmenities();
    List<Amenity> getAmenityByCategory(String category);
    Amenity addAmenity(AddAmenityRequest amenity);
    Amenity updateAmenity(UpdateAmenityRequest request, Long roomId);
    void deleteAmenity(Long amenityId);
    List<AmenityDto> getConvertedAmenity(List<Amenity> amenities);
    AmenityDto convertDto(Amenity amenity);
}
