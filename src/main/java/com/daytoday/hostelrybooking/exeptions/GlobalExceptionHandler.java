package com.daytoday.hostelrybooking.exeptions;

import com.daytoday.hostelrybooking.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
    String message = "You are not authorized";
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(message, null));
  }
}
