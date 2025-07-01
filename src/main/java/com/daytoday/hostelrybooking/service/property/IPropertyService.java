package com.daytoday.hostelrybooking.service.property;

import com.daytoday.hostelrybooking.dto.PropertyDto;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.request.CreatePropertyRequest;
import com.daytoday.hostelrybooking.request.UpdatePropertyRequest;

import java.util.List;

public interface IPropertyService {
    Property getPropertyById (Long propertyId);
    List<Property> getUserProperty (Long userId);
    List<Property> getPropertyByCity (String city);
    List<Property> getPropertyByCountry (String country);
    Property addProperty(CreatePropertyRequest request);
    Property updateProperty(UpdatePropertyRequest request, Long propertyId);
    void deletePropertyById(Long propertyId);
    List<PropertyDto> getConvertedProperty(List<Property> properties);
    PropertyDto convertDto(Property property);
}
