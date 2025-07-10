package com.daytoday.hostelrybooking.service.user;

import com.daytoday.hostelrybooking.request.AddUserRequest;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(AddUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);
}
