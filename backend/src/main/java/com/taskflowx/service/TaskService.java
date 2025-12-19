package com.taskflowx.service;

import com.taskflowx.dto.response.TaskResponse;
import com.taskflowx.enums.TaskStatus;
import com.taskflowx.exception.InvalidStateTransitionException;
import com.taskflowx.exception.ResourceNotFoundException;
import com.taskflowx.model.Task;
import com.taskflowx.model.User;
import com.taskflowx.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final AuditService auditService;

    @Cacheable(value = "tasks", key = "#taskId")
    @Transactional(readOnly = true)
    public Task getTaskById(Long taskId) {
        logger.info(">>> Fetching Task ID {} from Database (Cache Miss) <<<", taskId);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        // Force initialization of lazy relationships before caching
        if (task.getCreatedBy() != null) {
            task.getCreatedBy().getFullName();
        }
        if (task.getAssignedTo() != null) {
            task.getAssignedTo().getFullName();
        }
        return task;
    }

    @Cacheable(value = "tasksList", key = "#userId + '-' + #status")
    public List<TaskResponse> getTasksByUser(Long userId, TaskStatus status) {
        List<Task> tasks;
        if (status != null) {
            tasks = taskRepository.findByAssignedToIdAndStatus(userId, status);
        } else {
            tasks = taskRepository.findByAssignedToId(userId);
        }
        return tasks.stream().map(this::mapToTaskResponse).collect(Collectors.toList());
    }

    @Cacheable(value = "tasksList", key = "'manager-' + #managerId + '-' + #status")
    public List<TaskResponse> getTasksByManager(Long managerId, TaskStatus status) {
        List<Task> tasks;
        if (status != null) {
            tasks = taskRepository.findByCreatedByIdAndStatus(managerId, status);
        } else {
            tasks = taskRepository.findByCreatedById(managerId);
        }
        return tasks.stream().map(this::mapToTaskResponse).collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = {"tasks", "tasksList"}, allEntries = true)
    public void validateAndUpdateStatus(Task task, TaskStatus newStatus, User user, String comment) {
        TaskStatus oldStatus = task.getStatus();

        // Validate state transition
        validateStatusTransition(oldStatus, newStatus);

        task.setStatus(newStatus);
        taskRepository.save(task);

        // Async audit log
        String details = String.format("Status changed from %s to %s", oldStatus, newStatus);
        if (comment != null && !comment.isEmpty()) {
            details += ". Comment: " + comment;
        }
        auditService.logAction(user, task, "STATUS_CHANGED", details);
    }

    private void validateStatusTransition(TaskStatus current, TaskStatus next) {
        boolean isValid = switch (current) {
            case CREATED -> next == TaskStatus.ASSIGNED;
            case ASSIGNED -> next == TaskStatus.IN_PROGRESS;
            case IN_PROGRESS -> next == TaskStatus.SUBMITTED;
            case SUBMITTED -> next == TaskStatus.APPROVED || next == TaskStatus.REJECTED;
            case REJECTED -> next == TaskStatus.ASSIGNED;
            case APPROVED -> next == TaskStatus.CLOSED;
            case CLOSED -> false;
        };

        if (!isValid) {
            throw new InvalidStateTransitionException(
                    String.format("Invalid status transition from %s to %s", current, next)
            );
        }
    }

    public TaskResponse mapToTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedBy() != null ? task.getCreatedBy().getId() : null,
                task.getCreatedBy() != null ? task.getCreatedBy().getFullName() : null,
                task.getAssignedTo() != null ? task.getAssignedTo().getId() : null,
                task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : null,
                task.getRejectionReason(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}