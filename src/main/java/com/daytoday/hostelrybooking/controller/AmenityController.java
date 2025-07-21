package com.daytoday.hostelrybooking.controller;

import com.daytoday.hostelrybooking.dto.AmenityDto;
import com.daytoday.hostelrybooking.model.Amenity;
import com.daytoday.hostelrybooking.request.AddAmenityRequest;
import com.daytoday.hostelrybooking.request.UpdateAmenityRequest;
import com.daytoday.hostelrybooking.response.ApiResponse;
import com.daytoday.hostelrybooking.service.amenity.IAmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/amenities")
public class AmenityController {
  private final IAmenityService amenityService;

  @GetMapping("/")
  public ResponseEntity<ApiResponse> getAllAmenities() {
    try {
      List<Amenity> amenities = amenityService.getAllAmenities();
      if (amenities.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Amenities not found", null));
      }
      List<AmenityDto> convertedAmenities = amenityService.getConvertedAmenity(amenities);
      return ResponseEntity.ok(new ApiResponse("Success get all amenities", convertedAmenities));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/by/category/{category}")
  public ResponseEntity<ApiResponse> getAmenityByCategory(@PathVariable String category) {
    try {
      List<Amenity> amenities = amenityService.getAmenityByCategory(category);
      if (amenities.isEmpty()) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Amenities not found", null));
      }
      List<AmenityDto> convertedAmenities = amenityService.getConvertedAmenity(amenities);
      return ResponseEntity.ok(new ApiResponse("Success get amenity by category", convertedAmenities));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addAmenity(@RequestBody AddAmenityRequest request) {
    try {
      Amenity amenity = amenityService.addAmenity(request);
      AmenityDto convertedAmenity = amenityService.convertDto(amenity);
      return ResponseEntity.status(CREATED).body(new ApiResponse("Success add new amenity", convertedAmenity));
    } catch (Exception e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/amenity/{amenityId}/update")
  public ResponseEntity<ApiResponse> updateAmenity(@RequestBody UpdateAmenityRequest request, @PathVariable UUID amenityId) {
    try {
      Amenity amenity = amenityService.updateAmenity(request, amenityId);
      AmenityDto convertedAmenity = amenityService.convertDto(amenity);
      return ResponseEntity.ok(new ApiResponse("Success update existing amenity", convertedAmenity));
    } catch (Exception e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/amenity/{amenityId}/delete")
  public ResponseEntity<ApiResponse> deleteAmenity(@PathVariable UUID amenityId) {
    try {
      amenityService.deleteAmenity(amenityId);
      return ResponseEntity.ok(new ApiResponse("Success delete amenity", null));
    } catch (Exception e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
