package com.daytoday.hostelrybooking.service.user;

import com.daytoday.hostelrybooking.dto.UserDto;
import com.daytoday.hostelrybooking.exeptions.AlreadyExistsException;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.repository.UserRepository;
import com.daytoday.hostelrybooking.request.AddUserRequest;
import com.daytoday.hostelrybooking.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;

  @Override
  public User getUserById(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
  }

  @Override
  public User createUser(AddUserRequest request) {
    return Optional.of(request).filter(user -> !userRepository.existsByEmail(request.getEmail()))
        .map(req -> {
          User user = new User();
          user.setEmail(req.getEmail());
          user.setPassword(passwordEncoder.encode(req.getPassword()));
          user.setFirstName(req.getFirstName());
          user.setLastName(req.getLastName());
          user.setRole(req.getRole());
          return userRepository.save(user);
        }).orElseThrow(() -> new AlreadyExistsException("This email " + request.getEmail() + "already exists!"));
  }

  @Override
  public User updateUser(UpdateUserRequest request) {
    User existingUser = getAuthenticatedUser();
    existingUser.setFirstName(request.getFirstName());
    existingUser.setLastName(request.getLastName());
    existingUser.setPhoneNumber(request.getPhoneNumber());
    return userRepository.save(existingUser);
  }

  @Override
  public void deleteUser(UUID userId) {
    userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
      throw new ResourceNotFoundException("User not found!");
    });
  }

  @Override
  public User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    return Optional.ofNullable(userRepository.findByEmail(email))
        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
  }

  @Override
  public UserDto convertToDto(User user) {
    return modelMapper.map(user, UserDto.class);
  }
}
