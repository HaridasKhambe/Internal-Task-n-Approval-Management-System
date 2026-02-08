package com.taskflowx.dto.response;

import com.taskflowx.enums.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  private Long id;
  private String email;
  private String fullName;
  private Role role;
  private Boolean isActive;
  private LocalDateTime createdAt;
}