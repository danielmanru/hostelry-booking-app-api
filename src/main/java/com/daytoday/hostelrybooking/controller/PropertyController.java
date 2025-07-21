package com.daytoday.hostelrybooking.controller;

import com.daytoday.hostelrybooking.dto.PropertyDto;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.request.AddPropertyRequest;
import com.daytoday.hostelrybooking.request.UpdatePropertyRequest;
import com.daytoday.hostelrybooking.response.ApiResponse;
import com.daytoday.hostelrybooking.service.property.IPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/properties")
public class PropertyController {
    private final IPropertyService propertyService;

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable UUID propertyId) {
        try {
            Property property = propertyService.getPropertyById(propertyId);
            PropertyDto propertyDto = propertyService.convertDto(property);
            return ResponseEntity.ok(new ApiResponse("Success", propertyDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{userId}/properties")
    public ResponseEntity<ApiResponse> getPropertyByUserId(@PathVariable UUID userId) {
        try {
            List<Property> properties = propertyService.getPropertyByUserId(userId);
            if (properties.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Property not found!", null));
            }
            List<PropertyDto> convertedProperties = propertyService.getConvertedProperty(properties);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProperties));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/user/properties")
    public ResponseEntity<ApiResponse> getUserProperty() {
        try {
            List<Property> properties = propertyService.getUserProperty();
            if (properties.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Property not found!", null));
            }
            List<PropertyDto> convertedProperties = propertyService.getConvertedProperty(properties);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProperties));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/city/{city}")
    public ResponseEntity<ApiResponse> getPropertyByCity(@PathVariable String city) {
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

    @GetMapping("/by/country/{country}")
    public ResponseEntity<ApiResponse> getPropertyByCountry(@PathVariable String country) {
        try {
            List<Property> properties = propertyService.getPropertyByCountry(country);
            if (properties.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Property not found!", null));
            }
            List<PropertyDto> convertedProperties = propertyService.getConvertedProperty(properties);
            return ResponseEntity.ok(new ApiResponse("Success", convertedProperties));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProperty(@RequestBody AddPropertyRequest request) {
        try {
            Property property = propertyService.addProperty(request);
            PropertyDto propertyDto = propertyService.convertDto(property);
            return ResponseEntity.status(CREATED).body(new ApiResponse("Add Property Success", propertyDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN')")
    @PutMapping("/property/{propertyId}/update")
    public ResponseEntity<ApiResponse> updateProperty(@RequestBody UpdatePropertyRequest request, @PathVariable UUID propertyId) {
        try {
            Property property = propertyService.updateProperty(request, propertyId);
            PropertyDto propertyDto = propertyService.convertDto(property);
            return ResponseEntity.ok(new ApiResponse("Update Property Success", propertyDto));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_ADMIN')")
    @DeleteMapping("/property/{propertyId}/delete")
    public ResponseEntity<ApiResponse> deleteProperty(@PathVariable UUID propertyId) {
        try {
            propertyService.deletePropertyById(propertyId);
            return ResponseEntity.ok(new ApiResponse("Success delete property!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
