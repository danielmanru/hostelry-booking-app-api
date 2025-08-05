package com.daytoday.hostelrybooking.service.wishlist;

import com.daytoday.hostelrybooking.dto.PaymentDto;
import com.daytoday.hostelrybooking.dto.WishlistDto;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Payment;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.repository.PropertyRepository;
import com.daytoday.hostelrybooking.repository.UserRepository;
import com.daytoday.hostelrybooking.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistService implements IWishlistService {
  private final PropertyRepository propertyRepository;
  private final IUserService userService;
  private final ModelMapper modelMapper;
  private final UserRepository userRepository;

  @Override
  public void addWishlist(UUID propertyId) {
    User user = userService.getAuthenticatedUser();
    Property property = propertyRepository.findById(propertyId)
        .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
    user.addWishlist(property);
    userRepository.save(user);
  }

  @Override
  public void removeWishlist(UUID propertyId) {
    User user = userService.getAuthenticatedUser();
    Property property = propertyRepository.findById(propertyId)
        .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
    user.removeWishlist(property);
    userRepository.save(user);
  }

  @Override
  public List<Property> getUserWishlist() {
    User user = userService.getAuthenticatedUser();
    return user.getFavoriteProperties();
  }

  @Override
  public List<WishlistDto> getConvertedWishlist(List<Property> properties) {
    return properties.stream().map(this :: convertToDto).toList();
  }

  @Override
  public WishlistDto convertToDto(Property property) {
    return modelMapper.map(property, WishlistDto.class);
  }
}
