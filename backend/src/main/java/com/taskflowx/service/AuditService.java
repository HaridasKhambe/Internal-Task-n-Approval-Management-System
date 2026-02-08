package com.taskflowx.service;

import com.taskflowx.dto.response.AuditLogResponse;
import com.taskflowx.model.AuditLog;
import com.taskflowx.model.Task;
import com.taskflowx.model.User;
import com.taskflowx.repository.AuditLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditService {

  private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

  private final AuditLogRepository auditLogRepository;

  @Async // Indicates that this method should run asynchronously
  @Transactional
  public void logAction(User user, Task task, String action, String details) {
    logger.info(">>> Async Audit Log running on thread: {} <<<", Thread.currentThread().getName());

    AuditLog auditLog = new AuditLog();
    auditLog.setPerformedBy(user);
    auditLog.setTask(task);
    auditLog.setAction(action);
    auditLog.setDetails(details);

    auditLogRepository.save(auditLog);
  }

  public List<AuditLogResponse> getAllAuditLogs() {
    return auditLogRepository.findAll().stream()
      .map(this::mapToAuditLogResponse)
      .collect(Collectors.toList());
  }

  public List<AuditLogResponse> getAuditLogsByTaskId(Long taskId) {
    return auditLogRepository.findByTaskId(taskId).stream()
      .map(this::mapToAuditLogResponse)
      .collect(Collectors.toList());
  }

  public List<AuditLogResponse> getAuditLogsByUserId(Long userId) {
    return auditLogRepository.findByPerformedById(userId).stream()
      .map(this::mapToAuditLogResponse)
      .collect(Collectors.toList());
  }

  public List<AuditLogResponse> getAuditLogsByDateRange(LocalDateTime startDate,
    LocalDateTime endDate) {
    return auditLogRepository.findByTimestampBetween(startDate, endDate).stream()
      .map(this::mapToAuditLogResponse)
      .collect(Collectors.toList());
  }

  private AuditLogResponse mapToAuditLogResponse(AuditLog log) {
    return new AuditLogResponse(
      log.getId(),
      log.getTask() != null ? log.getTask().getId() : null,
      log.getTask() != null ? log.getTask().getTitle() : null,
      log.getPerformedBy().getId(),
      log.getPerformedBy().getFullName(),
      log.getAction(),
      null, // oldValue - extracted from details if needed
      null, // newValue - extracted from details if needed
      log.getTimestamp()
    );
  }
}