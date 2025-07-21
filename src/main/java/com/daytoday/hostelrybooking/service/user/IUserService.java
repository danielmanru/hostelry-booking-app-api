package com.daytoday.hostelrybooking.service.user;

import com.daytoday.hostelrybooking.dto.UserDto;
import com.daytoday.hostelrybooking.request.AddUserRequest;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.request.UpdateUserRequest;

import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    User getUserById(UUID userId);
    User createUser(AddUserRequest request);
    User updateUser(UpdateUserRequest request);
    void deleteUser(UUID userId);
    User getAuthenticatedUser();
    UserDto convertToDto(User user);
}
