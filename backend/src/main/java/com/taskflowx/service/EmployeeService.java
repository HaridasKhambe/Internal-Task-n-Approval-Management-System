package com.taskflowx.service;

import com.taskflowx.dto.request.UpdateStatusRequest;
import com.taskflowx.dto.response.TaskResponse;
import com.taskflowx.enums.TaskStatus;
import com.taskflowx.exception.ResourceNotFoundException;
import com.taskflowx.exception.UnauthorizedException;
import com.taskflowx.model.Task;
import com.taskflowx.model.User;
import com.taskflowx.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

  private final TaskService taskService;
  private final UserRepository userRepository;

  public List<TaskResponse> getMyTasks(String employeeEmail, TaskStatus status) {
    User employee = userRepository.findByEmail(employeeEmail)
      .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

    return taskService.getTasksByUser(employee.getId(), status);
  }

  @Transactional(readOnly = true)
  public TaskResponse getTaskDetails(Long taskId, String employeeEmail) {
    Task task = taskService.getTaskById(taskId);
    User employee = userRepository.findByEmail(employeeEmail)
      .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

    // Verify task is assigned to this employee
    if (task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(employee.getId())) {
      throw new UnauthorizedException("You can only view your assigned tasks");
    }

    return taskService.mapToTaskResponse(task);
  }

  @Transactional
  public TaskResponse updateTaskStatus(Long taskId, UpdateStatusRequest request,
    String employeeEmail) {
    Task task = taskService.getTaskById(taskId);
    User employee = userRepository.findByEmail(employeeEmail)
      .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

    // Verify task is assigned to this employee
    if (task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(employee.getId())) {
      throw new UnauthorizedException("You can only update your assigned tasks");
    }

    TaskStatus requestedStatus = request.getStatus();

    // Employees can only move to IN_PROGRESS or SUBMITTED
    if (requestedStatus != TaskStatus.IN_PROGRESS && requestedStatus != TaskStatus.SUBMITTED) {
      throw new IllegalArgumentException(
        "Employees can only update status to IN_PROGRESS or SUBMITTED");
    }

    taskService.validateAndUpdateStatus(task, requestedStatus, employee, request.getComment());

    return taskService.mapToTaskResponse(task);
  }
}