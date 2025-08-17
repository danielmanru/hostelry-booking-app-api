package com.daytoday.hostelrybooking.controller;

import com.daytoday.hostelrybooking.dto.AmenityDto;
import com.daytoday.hostelrybooking.dto.WishlistDto;
import com.daytoday.hostelrybooking.model.Amenity;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.response.ApiResponse;
import com.daytoday.hostelrybooking.service.wishlist.IWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/wishlists")
public class WishlistController {
  private final IWishlistService wishlistService;

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addWishlist(@RequestParam UUID propertyId) {
    try {
      wishlistService.addWishlist(propertyId);
      return ResponseEntity.status(CREATED).body(new ApiResponse("Success", null));
    } catch (ResourceAccessException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ApiResponse> removeWishlist(@RequestParam UUID propertyId) {
    try {
      wishlistService.removeWishlist(propertyId);
      return ResponseEntity.ok(new ApiResponse("Success", null));
    } catch (ResourceAccessException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }


  @GetMapping("/user/me/wishlists")
  public ResponseEntity<ApiResponse> getUserWishlist() {
    try {
      List<Property> properties = wishlistService.getUserWishlist();
      if(properties.isEmpty()){
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Wishlist is empty", null));
      }
      List<WishlistDto> wishlistDtos = wishlistService.getConvertedWishlist(properties);
      return ResponseEntity.ok(new ApiResponse("Success", wishlistDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
