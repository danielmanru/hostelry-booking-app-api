package com.daytoday.hostelrybooking.dto;

import com.daytoday.hostelrybooking.enums.UserRoleEnum;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private UserRoleEnum role;
 }
