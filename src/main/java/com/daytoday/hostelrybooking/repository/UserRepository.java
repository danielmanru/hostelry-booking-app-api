package com.daytoday.hostelrybooking.repository;

import com.daytoday.hostelrybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository <User, UUID> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}
