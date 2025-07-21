package com.daytoday.hostelrybooking.service.property;

import com.daytoday.hostelrybooking.dto.PropertyDto;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.request.AddPropertyRequest;
import com.daytoday.hostelrybooking.request.UpdatePropertyRequest;

import java.util.List;
import java.util.UUID;

public interface IPropertyService {
    Property getPropertyById (UUID propertyId);
    List<Property> getPropertyByUserId (UUID userId);
    List<Property> getUserProperty ();
    List<Property> getPropertyByCity (String city);
    List<Property> getPropertyByCountry(String country);
    Property addProperty(AddPropertyRequest request);
    Property updateProperty(UpdatePropertyRequest request, UUID propertyId);
    void deletePropertyById(UUID propertyId);
    List<PropertyDto> getConvertedProperty(List<Property> properties);
    PropertyDto convertDto(Property property);
}
