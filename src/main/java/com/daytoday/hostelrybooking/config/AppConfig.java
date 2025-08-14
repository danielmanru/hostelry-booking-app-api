package com.daytoday.hostelrybooking.config;

import com.cloudinary.Cloudinary;
import com.daytoday.hostelrybooking.security.jwt.AuthTokenFilter;
import com.daytoday.hostelrybooking.security.jwt.JwtAuthEntryPoint;
import com.daytoday.hostelrybooking.security.jwt.JwtUtils;
import com.daytoday.hostelrybooking.security.user.WebUserDetailsService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class AppConfig {
  @Value("${cloudinary.url}")
  private String cloudinaryUrl;
  private final WebUserDetailsService userDetailsService;
  private final JwtAuthEntryPoint authEntryPoint;
  private final JwtUtils jwtUtils;

  private static final List<String> SECURED_URLS =
      List.of("/api/v1/users/**", "/api/v1/bookings/**");

  @Bean
  public Cloudinary cloudinary() {
    return new Cloudinary(cloudinaryUrl);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthTokenFilter authTokenFilter() {
    return new AuthTokenFilter(jwtUtils, userDetailsService);
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    var authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v1/amenities/**").hasRole("ADMIN")
            .requestMatchers("/api/v1/images/**").hasAnyRole("ADMIN","OWNER")
            .requestMatchers("/api/v1/wishlist/**").hasRole("USER")
            .requestMatchers("/api/v1/users/add").permitAll()
            .requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated()
            .anyRequest().permitAll());
    http.authenticationProvider(daoAuthenticationProvider());
    http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
