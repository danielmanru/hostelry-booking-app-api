package com.daytoday.hostelrybooking.service.image;

import com.daytoday.hostelrybooking.dto.ImageDto;
import com.daytoday.hostelrybooking.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    List<Image> getPropertyImages(Long propertyId);
    List<Image> getRoomImages(Long roomId);
    List<Image> savePropertyImages(Long propertyId, List<MultipartFile> files);
    List<Image> saveRoomImages(Long roomId, List<MultipartFile> files);
    void deleteImage(String filePublicId);
    List<ImageDto> getConvertedImages(List<Image> images);
}
