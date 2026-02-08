package com.taskflowx.dto.request;

import com.taskflowx.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

  @NotBlank(message = "Title is required")
  private String title;

  private String description;

  @NotNull(message = "Priority is required")
  private TaskPriority priority;

  private LocalDate dueDate;
}