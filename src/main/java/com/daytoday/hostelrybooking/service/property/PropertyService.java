package com.daytoday.hostelrybooking.service.property;

import com.daytoday.hostelrybooking.dto.ImageDto;
import com.daytoday.hostelrybooking.dto.PropertyDto;
import com.daytoday.hostelrybooking.dto.RoomDto;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Image;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.repository.PropertyRepository;
import com.daytoday.hostelrybooking.request.CreatePropertyRequest;
import com.daytoday.hostelrybooking.request.UpdatePropertyRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService implements IPropertyService{
    private final PropertyRepository propertyRepository;
    private final ModelMapper modelMapper;

    @Override
    public Property getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
    }

    @Override
    public List<Property> getUserProperty(Long userId) {
        return propertyRepository.findByOwner(userId);
    }

    @Override
    public List<Property> getPropertyByCity(String city) {
        return propertyRepository.findByCity(city);
    }

    @Override
    public List<Property> getPropertyByCountry(String country) {
        return propertyRepository.findByCountry(country);
    }

    @Override
    public Property addProperty(CreatePropertyRequest request) {
        return propertyRepository.save(createProperty(request));
    }

    private Property createProperty(CreatePropertyRequest request) {
        return new Property(
                request.getOwner(),
                request.getName(),
                request.getDescription(),
                request.getAddress(),
                request.getCity(),
                request.getCountry(),
                request.getType()
        );
    }

    @Override
    public Property updateProperty(UpdatePropertyRequest request, Long propertyId) {
        return propertyRepository.findById(propertyId)
                .map(existingProduct -> updateExistingProperty(existingProduct, request))
                .map(propertyRepository :: save)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found!"));
    }

    private Property updateExistingProperty(Property existingProperty, UpdatePropertyRequest request) {
        existingProperty.setName(request.getName());
        existingProperty.setDescription(request.getDescription());
        existingProperty.setAddress(request.getAddress());
        existingProperty.setCity(request.getCity());
        existingProperty.setCountry(request.getCountry());
        existingProperty.setType(request.getType());

        return existingProperty;
    }

    @Override
    public void deletePropertyById(Long propertyId) {
        propertyRepository.findById(propertyId)
                .ifPresentOrElse(propertyRepository :: delete, () -> {
                    throw new ResourceNotFoundException("Property not Found!");
                });
    }

    @Override
    public List<PropertyDto> getConvertedProperty(List<Property> properties) {
        return properties.stream().map(this :: convertDto).toList();
    }

    @Override
    public PropertyDto convertDto(Property property) {
        PropertyDto propertyDto = modelMapper.map(property, PropertyDto.class);
        RoomDto roomDto = modelMapper.map(room, RoomDto.class);
        List<Image> images = roomRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        propertyDto.setImages(imageDtos);
        return propertyDto;
    }
}
