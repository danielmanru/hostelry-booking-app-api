package com.daytoday.hostelrybooking.controller;

import com.daytoday.hostelrybooking.dto.ImageDto;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.response.ApiResponse;
import com.daytoday.hostelrybooking.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
  private final IImageService imageService;

  @PostMapping("/add/property/images")
  public ResponseEntity<ApiResponse> addPropertyImages(@RequestParam List<MultipartFile> images, @RequestParam UUID propertyId) {
    try {
      List<ImageDto> imageDtos = imageService.addPropertyImages(propertyId, images);
      return ResponseEntity.status(CREATED).body(new ApiResponse("Success", imageDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PostMapping("/add/room/images")
  public ResponseEntity<ApiResponse> addRoomImages(@RequestParam List<MultipartFile> images, @RequestParam UUID roomId) {
    try {
      List<ImageDto> imageDtos = imageService.addRoomImages(roomId, images);
      return ResponseEntity.status(CREATED).body(new ApiResponse("Success", imageDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ApiResponse> deleteImage(@RequestParam String imagePublicIds) {
    try {
      imageService.deleteImage(imagePublicIds);
      return ResponseEntity.ok(new ApiResponse("Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Image not found", null));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }

  }
}
