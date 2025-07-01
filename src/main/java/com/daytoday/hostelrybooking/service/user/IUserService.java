package com.daytoday.hostelrybooking.service.user;

import com.daytoday.hostelrybooking.request.CreateUserRequest;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);
}
