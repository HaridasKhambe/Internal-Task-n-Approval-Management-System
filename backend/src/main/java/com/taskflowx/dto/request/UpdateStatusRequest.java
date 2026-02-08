package com.taskflowx.dto.request;

import com.taskflowx.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {

  @NotNull(message = "Status is required")
  private TaskStatus status;

  private String comment; // Optional: for additional notes when updating status
}