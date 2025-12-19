package com.taskflowx.controller;

import com.taskflowx.dto.request.ApproveRejectRequest;
import com.taskflowx.dto.request.AssignTaskRequest;
import com.taskflowx.dto.request.CreateTaskRequest;
import com.taskflowx.dto.response.ApiResponse;
import com.taskflowx.dto.response.TaskResponse;
import com.taskflowx.enums.TaskStatus;
import com.taskflowx.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/tasks")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication
    ) {
        String managerEmail = authentication.getName();
        TaskResponse response = managerService.createTask(request, managerEmail);
        return ResponseEntity.ok(ApiResponse.success("Task created successfully", response));
    }

    @PutMapping("/tasks/{id}/assign")
    public ResponseEntity<ApiResponse<TaskResponse>> assignTask(
            @PathVariable Long id,
            @Valid @RequestBody AssignTaskRequest request,
            Authentication authentication
    ) {
        String managerEmail = authentication.getName();
        TaskResponse response = managerService.assignTask(id, request, managerEmail);
        return ResponseEntity.ok(ApiResponse.success("Task assigned successfully", response));
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getMyTasks(
            @RequestParam(required = false) TaskStatus status,
            Authentication authentication
    ) {
        String managerEmail = authentication.getName();
        List<TaskResponse> tasks = managerService.getMyTasks(managerEmail, status);
        return ResponseEntity.ok(ApiResponse.success("Tasks retrieved successfully", tasks));
    }

    @PutMapping("/tasks/{id}/review")
    public ResponseEntity<ApiResponse<TaskResponse>> reviewTask(
            @PathVariable Long id,
            @Valid @RequestBody ApproveRejectRequest request,
            Authentication authentication
    ) {
        String managerEmail = authentication.getName();
        TaskResponse response = managerService.reviewTask(id, request, managerEmail);
        return ResponseEntity.ok(ApiResponse.success("Task reviewed successfully", response));
    }
}