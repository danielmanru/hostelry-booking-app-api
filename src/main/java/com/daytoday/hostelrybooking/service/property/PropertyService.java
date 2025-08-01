package com.daytoday.hostelrybooking.service.property;

import com.daytoday.hostelrybooking.dto.ImageDto;
import com.daytoday.hostelrybooking.dto.PropertyDto;
import com.daytoday.hostelrybooking.enums.PropertyTypeEnum;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Image;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.repository.ImageRepository;
import com.daytoday.hostelrybooking.repository.PropertyRepository;
import com.daytoday.hostelrybooking.request.AddPropertyRequest;
import com.daytoday.hostelrybooking.request.UpdatePropertyRequest;
import com.daytoday.hostelrybooking.service.user.IUserService;
import com.daytoday.hostelrybooking.specification.PropertySpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PropertyService implements IPropertyService{
    private final PropertyRepository propertyRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final IUserService userService;

    @Override
    public Property getPropertyById(UUID propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
    }

    @Override
    public List<Property> getUserProperty() {
        User user = userService.getAuthenticatedUser();
        return propertyRepository.findByOwnerId(user.getId());
    }

    @Override
    public List<Property> getPropertyByUserId(UUID userId) {
        return propertyRepository.findByOwnerId(userId);
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
    public List<Property> searchProperty(String city, int guestCount, int roomCount, String sortField, String sortDirection, int page, int size) {
        Specification<Property> spec = PropertySpecification.hasCity(city)
            .and(PropertySpecification.hasRoomWithMinGuestAndUnits(guestCount, roomCount));

      spec = switch (sortField.toLowerCase()) {
        case "lowest_price" -> spec.and(PropertySpecification.orderByLowestPrice());
        case "highest_price" -> spec.and(PropertySpecification.orderByHighestPrice());
        default -> spec.and(PropertySpecification.orderByHighestRating());
      };

        Pageable pageable = PageRequest.of(page, size);

        Page<Property> pagedResult= propertyRepository.findAll(spec, pageable);
      return pagedResult.getContent();
    }

    @Override
    public Property addProperty(AddPropertyRequest request) {
        User user = userService.getAuthenticatedUser();
        return propertyRepository.save(createProperty(request, user));
    }

    private Property createProperty(AddPropertyRequest request, User user) {
        return new Property(
                user,
                request.getName(),
                request.getDescription(),
                request.getAddress(),
                request.getCity(),
                request.getCountry(),
                PropertyTypeEnum.valueOf(request.getType())
        );
    }

    @Override
    public Property updateProperty(UpdatePropertyRequest request, UUID propertyId) {
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
    public void deletePropertyById(UUID propertyId) {
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
        List<Image> images = imageRepository.findByPropertyId(property.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        propertyDto.setImages(imageDtos);
        return propertyDto;
    }
}
