package com.verifico.server.auth.reset;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.verifico.server.auth.dto.ResetPasswordRequest;
import com.verifico.server.auth.dto.ResetRequest;
import com.verifico.server.common.dto.APIResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Forgot password API Endpoints", description = "Endpoints for when user tries to hit forgot password.. Ensuring logical and secure steps")
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  @Operation(summary = "Request password reset email")
  @PostMapping("/reset-password")
  public ResponseEntity<APIResponse<String>> requestPassReset(@Valid @RequestBody ResetRequest request) {
    passwordResetService.passResetRequest(request.getEmail());
    return ResponseEntity.ok()
        .body(new APIResponse<>("If the email is associated with an account, you'll get a reset link", null));
  }

  @Operation(summary = "Validate password reset token")
  @GetMapping("/reset-password")
  public ResponseEntity<APIResponse<String>> validateToken(@RequestParam("token") String token) {
    passwordResetService.validateToken(token);

    return ResponseEntity.ok()
        .body(new APIResponse<>("Valid Token", null));
  }

  @Operation(summary = "Confirm and apply new password")
  @PostMapping("/reset-password/confirm")
  public ResponseEntity<APIResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    passwordResetService.resetPassword(request);

    return ResponseEntity.ok()
        .body(new APIResponse<>("Password Successfully Updated", null));
  }

}
