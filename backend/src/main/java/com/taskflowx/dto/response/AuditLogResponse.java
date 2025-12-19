package com.taskflowx.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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