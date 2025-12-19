package com.taskflowx.controller;

import com.taskflowx.dto.request.UpdateStatusRequest;
import com.taskflowx.dto.response.ApiResponse;
import com.taskflowx.dto.response.TaskResponse;
import com.taskflowx.enums.TaskStatus;
import com.taskflowx.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getMyTasks(
            @RequestParam(required = false) TaskStatus status,
            Authentication authentication
    ) {
        String employeeEmail = authentication.getName();
        List<TaskResponse> tasks = employeeService.getMyTasks(employeeEmail, status);
        return ResponseEntity.ok(ApiResponse.success("Tasks retrieved successfully", tasks));
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String employeeEmail = authentication.getName();
        TaskResponse task = employeeService.getTaskDetails(id, employeeEmail);
        return ResponseEntity.ok(ApiResponse.success("Task retrieved successfully", task));
    }

    @PutMapping("/tasks/{id}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request,
            Authentication authentication
    ) {
        String employeeEmail = authentication.getName();
        TaskResponse response = employeeService.updateTaskStatus(id, request, employeeEmail);
        return ResponseEntity.ok(ApiResponse.success("Task status updated successfully", response));
    }
}
