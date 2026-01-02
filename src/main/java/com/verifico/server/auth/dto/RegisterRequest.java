package com.verifico.server.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

  @NotBlank(message = "Username required")
  @Size(min = 3, max = 16, message = "Username must be 3–16 characters")
  private String username;

  @NotBlank(message = "First name required")
  private String firstName;

  @NotBlank(message = "Last name required")
  private String lastName;

  @NotBlank(message = "Email required")
  @Email(message = "Email must be valid")
  private String email;

  @NotBlank(message = "Password required")
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String password;

  @Size(max = 500, message = "Bio must not exceed 500 characters")
  private String bio;

  private String avatarUrl;
}
