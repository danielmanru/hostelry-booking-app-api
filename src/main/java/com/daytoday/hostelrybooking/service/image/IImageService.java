package com.daytoday.hostelrybooking.service.image;

import com.daytoday.hostelrybooking.dto.ImageDto;
import com.daytoday.hostelrybooking.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IImageService {
//    List<Image> getPropertyImages(UUID propertyId);
//    List<Image> getRoomImages(UUID roomId);
    List<ImageDto> addPropertyImages(UUID propertyId, List<MultipartFile> files);
    List<ImageDto> addRoomImages(UUID roomId, List<MultipartFile> files);
    void deleteImage(String filePublicId);
//    List<ImageDto> getConvertedImages(List<Image> images);
}
