package com.daytoday.hostelrybooking.controller;

import com.daytoday.hostelrybooking.dto.PropertyDto;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.request.CreatePropertyRequest;
import com.daytoday.hostelrybooking.request.UpdatePropertyRequest;
import com.daytoday.hostelrybooking.response.ApiResponse;
import com.daytoday.hostelrybooking.service.property.IPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("@{api.prefix/properties}")
public class PropertyController {
    private final IPropertyService propertyService;

    @GetMapping("property/{propertyId}/property")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long propertyId) {
        try {
            Property property = propertyService.getPropertyById(propertyId);
            PropertyDto propertyDto = propertyService.convertDto(property);
            return ResponseEntity.ok(new ApiResponse("Success", propertyDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("user/{userId}/properties")
    public ResponseEntity<ApiResponse> getUserProperty(@PathVariable Long userId) {
        try {
            List<Property> properties = propertyService.getUserProperty(userId);
            if (properties.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Property not found!", null));
            }
            List<PropertyDto> convertedProperties = propertyService.getConvertedProperty(properties);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProperties));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("by/city")
    public ResponseEntity<ApiResponse> getPropertyByCity(@RequestParam String city) {
        try {
            List<Property> properties = propertyService.getPropertyByCity(city);
            if (properties.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Property not found!", null));
            }
            List<PropertyDto> convertedProperties = propertyService.getConvertedProperty(properties);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProperties));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("by/country")
    public ResponseEntity<ApiResponse> getPropertyByCountry(@RequestParam String country) {
        try {
            List<Property> properties = propertyService.getPropertyByCity(country);
            if (properties.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Property not found!", null));
            }
            List<PropertyDto> convertedProperties = propertyService.getConvertedProperty(properties);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProperties));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProperty(@RequestBody CreatePropertyRequest request) {
        try {
            Property property = propertyService.addProperty(request);
            PropertyDto propertyDto = propertyService.convertDto(property);
            return ResponseEntity.ok(new ApiResponse("Add Property Success", propertyDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/property/{propertyId}/update")
    public ResponseEntity<ApiResponse> updateProperty(@RequestBody UpdatePropertyRequest request, @PathVariable Long propertyId) {
        try {
            Property property = propertyService.updateProperty(request, propertyId);
            PropertyDto propertyDto = propertyService.convertDto(property);
            return ResponseEntity.ok(new ApiResponse("Update Property Success", propertyDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/property/{propertyId}/delete")
    public ResponseEntity<ApiResponse> deleteProperty(@PathVariable Long propertyId) {
        try {
            propertyService.deletePropertyById(propertyId);
            return ResponseEntity.ok(new ApiResponse("Success delete property!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
