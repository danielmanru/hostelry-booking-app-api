package com.daytoday.hostelrybooking.controller;

import com.daytoday.hostelrybooking.exeptions.AlreadyExistsException;
import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.request.AddUserRequest;
import com.daytoday.hostelrybooking.request.UpdateUserRequest;
import com.daytoday.hostelrybooking.response.ApiResponse;
import com.daytoday.hostelrybooking.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(new ApiResponse("Success", user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody AddUserRequest request) {
        try {
            User user = userService.createUser(request);
            return ResponseEntity.status(CREATED).body(new ApiResponse("User created successfully", user));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable UpdateUserRequest request, Long userId) {
        try {
            User user = userService.updateUser(request, userId);
            return ResponseEntity.ok(new ApiResponse("Update User Success", user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
