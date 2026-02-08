package com.taskflowx.dto.response;

import com.taskflowx.enums.TaskPriority;
import com.taskflowx.enums.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

  private Long id;
  private String title;
  private String description;
  private TaskStatus status;
  private TaskPriority priority;
  private LocalDate dueDate;

  // Creator info
  private Long createdById;
  private String createdByName;

  // Assignee info
  private Long assignedToId;
  private String assignedToName;

  private String rejectionReason;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
