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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IPropertyService propertyService;
    private final IRoomService roomService;
    private final Cloudinary cloudinary;
    private final ModelMapper modelMapper;

    @Override
    public List<Image> getPropertyImages(Long propertyId) {
        return imageRepository.findByPropertyId(propertyId);
    }

    @Override
    public List<Image> getRoomImages(Long roomId) {
        return imageRepository.findByRoomId(roomId);
    }

    @Override
    public List<Image> savePropertyImages(Long propertyId, List<MultipartFile> files) {
        Property property = propertyService.getPropertyById(propertyId);

        List<Image> savedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                Map<String, Object> params = Map.of(
                        "asset_folder", "hostelry-booking-assets",
                        "use_filename", true
                );
                Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), params);
                image.setFileName(file.getOriginalFilename());
                image.setFilePublicId(result.get("public_id").toString());
                image.setImageUrl(result.get("secure_url").toString());
                image.setProperty(property);
                image.setRoom(null);
                Image savedImage = imageRepository.save(image);
                savedImages.add(savedImage);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImages;
    }

    @Override
    public List<Image> saveRoomImages(Long roomId, List<MultipartFile> files) {
        Room room = roomService.getRoomById(roomId);

        List<Image> savedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                Map<String, Object> params = Map.of(
                        "asset_folder", "hostelry-booking-assets",
                        "use_filename", true
                );
                Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), params);
                image.setFileName(file.getOriginalFilename());
                image.setFilePublicId(result.get("public_id").toString());
                image.setImageUrl(result.get("secure_url").toString());
                image.setRoom(room);
                image.setProperty(null);
                Image savedImage = imageRepository.save(image);
                savedImages.add(savedImage);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImages;
    }


    @Override
    public void deleteImage(String filePublicId) {
        try {
            imageRepository.findByFilePublicId(filePublicId)
                    .ifPresentOrElse(imageRepository :: delete, () ->{
                        throw new ResourceNotFoundException("Image not found");
                    });
            cloudinary.uploader().destroy(filePublicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ImageDto> getConvertedImages(List<Image> images) {
        return images.stream().map(image ->
            modelMapper.map(image, ImageDto.class)
        ).toList();
    }
}
