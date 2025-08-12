package com.daytoday.hostelrybooking.data;

import com.daytoday.hostelrybooking.enums.BedTypeEnum;
import com.daytoday.hostelrybooking.enums.PropertyTypeEnum;
import com.daytoday.hostelrybooking.enums.UserRoleEnum;
import com.daytoday.hostelrybooking.model.Amenity;
import com.daytoday.hostelrybooking.model.Property;
import com.daytoday.hostelrybooking.model.Room;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.repository.AmenityRepository;
import com.daytoday.hostelrybooking.repository.PropertyRepository;
import com.daytoday.hostelrybooking.repository.RoomRepository;
import com.daytoday.hostelrybooking.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
  private final UserRepository userRepository;
  private final AmenityRepository amenityRepository;
  private final PropertyRepository propertyRepository;
  private final RoomRepository roomRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
    List<User> users = createExampleUserIfNotExists();
    List<Amenity> amenities = createExampleAmenityIfNotExists();
    List<Property> properties = createExamplePropertyIfNotExists(users);
    createExampleRoomIfNotExists(properties, amenities);
  }

  private List<User> createExampleUserIfNotExists(){
    List<User> users = new ArrayList<>();
    for(int i=0; i<2; i++){
      String exampleEmail = "user"+i+"@gmail.com";
      if (userRepository.existsByEmail(exampleEmail)) {
        continue;
      }
      User user = new User();
      user.setFirstName("User");
      user.setLastName("No "+i);
      user.setEmail(exampleEmail);
      user.setPassword(passwordEncoder.encode("12345678"));
      user.setPhoneNumber("1234567891011");
      user.setRole(UserRoleEnum.ROLE_OWNER);
      userRepository.save(user);
      users.add(user);
    }
    return users;
  }

  private List<Amenity> createExampleAmenityIfNotExists() {
    List<Amenity> amenities = new ArrayList<>();
    for (int i=0; i<2; i++) {
      Amenity amenity = new Amenity();
      amenity.setCategory("category "+i);
      amenity.setName("amenity "+i);
      amenityRepository.save(amenity);
      amenities.add(amenity);
    }
    return amenities;
  }

  private List<Property> createExamplePropertyIfNotExists(List<User> users) {
    List<Property> properties = new ArrayList<>();
    for (int i=0; i<users.size(); i++) {
      Property property = new Property();
      property.setOwner(users.get(i));
      property.setName("property "+i);
      property.setDescription("Description for property "+i);
      property.setAddress("Address for property "+ i);
      property.setCity("Bandung");
      property.setCountry("Indonesia");
      property.setType(PropertyTypeEnum.HOTEL);
      propertyRepository.save(property);
      properties.add(property);
    }
    return properties;
  }

  private void createExampleRoomIfNotExists(List<Property> properties, List<Amenity> amenities) {
    for (int i=0; i<properties.size(); i++) {
      Room room = new Room();
      room.setProperty(properties.get(i));
      room.setRoomName("Room "+i);
      room.setDescription("Room "+i+" Description");
      room.setMaxGuest(2);
      room.setPricePerNight(new BigDecimal(500000));
      room.setBedType1(BedTypeEnum.DOUBLE);
      room.setUnitAvailable(10);
      room.setRoomSize(30.0);
      room.setAmenities(amenities);
      roomRepository.save(room);
    }
  }
}
