package com.daytoday.hostelrybooking.service.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.daytoday.hostelrybooking.dto.ImageDto;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.Image;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.model.Room;
import com.daytoday.hostelrybooking.repository.ImageRepository;
import com.daytoday.hostelrybooking.service.property.IPropertyService;
import com.daytoday.hostelrybooking.service.room.IRoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
  private final ImageRepository imageRepository;
  private final IPropertyService propertyService;
  private final IRoomService roomService;
  private final Cloudinary cloudinary;
  private final ModelMapper modelMapper;

  //    @Override
//    public List<Image> getPropertyImages(UUID propertyId) {
//        return imageRepository.findByPropertyId(propertyId);
//    }
//
//    @Override
//    public List<Image> getRoomImages(UUID roomId) {
//        return imageRepository.findByRoomId(roomId);
//    }
  private Map<String, Object> uploadImages(MultipartFile file, String folderName, String defaultDisplayName) {
    String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse(defaultDisplayName);
    try {
      Map<String, Object> params = Map.of(
          "asset_folder", "hostelry-booking-assets/"+folderName,
          "use_filename", true,
          "display_name", fileName,
          "use_asset_folder_as_public_id_prefix", true,
          "unique_filename", true
      );

      return (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(), params);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }


  @Override
  public List<ImageDto> addPropertyImages(UUID propertyId, List<MultipartFile> files) {
    Property property = propertyService.getPropertyById(propertyId);

    List<ImageDto> savedImageDto = new ArrayList<>();
    for (MultipartFile file : files) {

      Image image = new Image();
      Map<String, Object> result = uploadImages(file, "property-assets", property.getName());
      image.setFileName(file.getOriginalFilename());
      image.setFilePublicId(result.get("public_id").toString());
      image.setImageUrl(result.get("secure_url").toString());
      image.setProperty(property);
      image.setRoom(null);
      Image savedImage = imageRepository.save(image);
      savedImageDto.add(modelMapper.map(savedImage, ImageDto.class));
    }
    return savedImageDto;
  }

  @Override
  public List<ImageDto> addRoomImages(UUID roomId, List<MultipartFile> files) {
    Room room = roomService.getRoomById(roomId);

    List<ImageDto> savedImageDto = new ArrayList<>();
    for (MultipartFile file : files) {

      Image image = new Image();
      Map<String, Object> result = uploadImages(file, "room-assets", room.getRoomName());
      image.setFileName(file.getOriginalFilename());
      image.setFilePublicId(result.get("public_id").toString());
      image.setImageUrl(result.get("secure_url").toString());
      image.setRoom(room);
      image.setProperty(null);
      Image savedImage = imageRepository.save(image);
      savedImageDto.add(modelMapper.map(savedImage, ImageDto.class));
    }
    return savedImageDto;
  }


  @Override
  public void deleteImage(String filePublicIds) {
    String[] publicIds = filePublicIds.split(",");
    for (String publicId : publicIds) {
      try {
        imageRepository.findByFilePublicId(publicId)
            .ifPresentOrElse(imageRepository::delete, () -> {
              throw new ResourceNotFoundException("Image not found");
            });
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
      } catch (IOException e) {
        throw new RuntimeException(e.getMessage());
      }
    }
  }

//    @Override
//    public List<ImageDto> getConvertedImages(List<Image> images) {
//        return images.stream().map(image ->
//            modelMapper.map(image, ImageDto.class)
//        ).toList();
//    }
}
