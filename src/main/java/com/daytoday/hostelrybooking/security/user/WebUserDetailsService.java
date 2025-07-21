package com.daytoday.hostelrybooking.security.user;

import com.daytoday.hostelrybooking.exeptions.ResourceNotFoundException;
import com.daytoday.hostelrybooking.model.User;
import com.daytoday.hostelrybooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = Optional.ofNullable(userRepository.findByEmail(email))
        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

    return WebUserDetails.buildUserDetails(user);
  }
}
