package com.daytoday.hostelrybooking.service.wishlist;

import com.daytoday.hostelrybooking.dto.WishlistDto;
import com.daytoday.hostelrybooking.model.Property;

import java.util.List;
import java.util.UUID;

public interface IWishlistService {
  void addWishlist(UUID propertyId);
  void removeWishlist(UUID propertyId);
  List<Property> getUserWishlist();
  List<WishlistDto> getConvertedWishlist(List<Property> properties);
  WishlistDto convertToDto(Property property);
}
