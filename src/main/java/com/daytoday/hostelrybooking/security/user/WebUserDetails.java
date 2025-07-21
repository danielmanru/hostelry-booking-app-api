package com.daytoday.hostelrybooking.security.user;

import com.daytoday.hostelrybooking.model.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebUserDetails implements UserDetails {
  private UUID id;
  private String email;
  private String password;
  private Collection<GrantedAuthority> authority;

  public static WebUserDetails buildUserDetails(User user) {
    return new WebUserDetails(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        List.of(new SimpleGrantedAuthority(user.getRole()))
    );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authority;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}
