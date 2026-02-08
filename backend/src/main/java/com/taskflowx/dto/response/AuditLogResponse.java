package com.taskflowx.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {

  private Long id;
  private Long taskId;
  private String taskTitle;
  private Long userId;
  private String userName;
  private String action;
  private String oldValue;
  private String newValue;
  private LocalDateTime timestamp;
}