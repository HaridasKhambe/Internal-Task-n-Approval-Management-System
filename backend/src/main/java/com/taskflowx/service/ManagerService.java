package com.taskflowx.service;

import com.taskflowx.dto.request.ApproveRejectRequest;
import com.taskflowx.dto.request.AssignTaskRequest;
import com.taskflowx.dto.request.CreateTaskRequest;
import com.taskflowx.dto.response.TaskResponse;
import com.taskflowx.enums.Role;
import com.taskflowx.enums.TaskStatus;
import com.taskflowx.exception.ResourceNotFoundException;
import com.taskflowx.exception.UnauthorizedException;
import com.taskflowx.model.Task;
import com.taskflowx.model.User;
import com.taskflowx.repository.TaskRepository;
import com.taskflowx.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerService {

  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final TaskService taskService;
  private final AuditService auditService;

  @Transactional
  @CacheEvict(value = {"tasks", "tasksList"}, allEntries = true)
  public TaskResponse createTask(CreateTaskRequest request, String managerEmail) {
    User manager = userRepository.findByEmail(managerEmail)
      .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

    Task task = new Task();
    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setPriority(request.getPriority());
    task.setDueDate(request.getDueDate());
    task.setStatus(TaskStatus.CREATED);
    task.setCreatedBy(manager);

    Task savedTask = taskRepository.save(task);

    auditService.logAction(manager, savedTask, "TASK_CREATED",
      "Task created: " + savedTask.getTitle());

    return taskService.mapToTaskResponse(savedTask);
  }

  @Transactional
  @CacheEvict(value = {"tasks", "tasksList"}, allEntries = true)
  public TaskResponse assignTask(Long taskId, AssignTaskRequest request, String managerEmail) {
    Task task = taskService.getTaskById(taskId);
    User manager = userRepository.findByEmail(managerEmail)
      .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

    // Verify manager owns this task
    if (!task.getCreatedBy().getId().equals(manager.getId())) {
      throw new UnauthorizedException("You can only assign your own tasks");
    }

    User employee = userRepository.findById(request.getEmployeeId())
      .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

    if (employee.getRole() != Role.EMPLOYEE) {
      throw new IllegalArgumentException("Can only assign tasks to employees");
    }

    task.setAssignedTo(employee);
    task.setStatus(TaskStatus.ASSIGNED);
    taskRepository.save(task);

    auditService.logAction(manager, task, "TASK_ASSIGNED",
      "Task assigned to " + employee.getFullName());

    return taskService.mapToTaskResponse(task);
  }

  public List<TaskResponse> getMyTasks(String managerEmail, TaskStatus status) {
    User manager = userRepository.findByEmail(managerEmail)
      .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

    return taskService.getTasksByManager(manager.getId(), status);
  }

  @Transactional
  @CacheEvict(value = {"tasks", "tasksList"}, allEntries = true)
  public TaskResponse reviewTask(Long taskId, ApproveRejectRequest request, String managerEmail) {
    Task task = taskService.getTaskById(taskId);
    User manager = userRepository.findByEmail(managerEmail)
      .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

    // Verify manager owns this task
    if (!task.getCreatedBy().getId().equals(manager.getId())) {
      throw new UnauthorizedException("You can only review your own tasks");
    }

    if (task.getStatus() != TaskStatus.SUBMITTED) {
      throw new IllegalArgumentException("Can only review tasks in SUBMITTED status");
    }

    String action = request.getAction().toUpperCase();

    if (action.equals("APPROVE")) {
      task.setStatus(TaskStatus.APPROVED);
      task.setRejectionReason(null);
      taskRepository.save(task);

      auditService.logAction(manager, task, "TASK_APPROVED",
        "Task approved" + (request.getComment() != null ? ". Comment: " + request.getComment()
          : ""));

    } else if (action.equals("REJECT")) {
      if (request.getComment() == null || request.getComment().trim().isEmpty()) {
        throw new IllegalArgumentException("Rejection reason is required");
      }

      task.setStatus(TaskStatus.REJECTED);
      task.setRejectionReason(request.getComment());
      taskRepository.save(task);

      // After rejection, move back to ASSIGNED for rework
      task.setStatus(TaskStatus.ASSIGNED);
      taskRepository.save(task);

      auditService.logAction(manager, task, "TASK_REJECTED",
        "Task rejected. Reason: " + request.getComment());

    } else {
      throw new IllegalArgumentException("Action must be APPROVE or REJECT");
    }

    return taskService.mapToTaskResponse(task);
  }
}