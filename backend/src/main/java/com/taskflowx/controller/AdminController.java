package com.taskflowx.controller;

import com.taskflowx.dto.request.CreateUserRequest;
import com.taskflowx.dto.response.ApiResponse;
import com.taskflowx.dto.response.AuditLogResponse;
import com.taskflowx.dto.response.UserResponse;
import com.taskflowx.service.AdminService;
import com.taskflowx.service.AuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final AuditService auditService;

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = adminService.createUser(request);
        return ResponseEntity.ok(ApiResponse.success("User created successfully", response));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = adminService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getAuditLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<AuditLogResponse> logs;

        if (taskId != null) {
            logs = auditService.getAuditLogsByTaskId(taskId);
        } else if (userId != null) {
            logs = auditService.getAuditLogsByUserId(userId);
        } else if (startDate != null && endDate != null) {
            logs = auditService.getAuditLogsByDateRange(startDate, endDate);
        } else {
            logs = auditService.getAllAuditLogs();
        }

        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", logs));
    }
}