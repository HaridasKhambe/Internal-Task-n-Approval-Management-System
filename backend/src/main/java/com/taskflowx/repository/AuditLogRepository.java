package com.taskflowx.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taskflowx.model.AuditLog;
import com.taskflowx.model.Task;
import com.taskflowx.model.User;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByTaskId(Long taskId);

    List<AuditLog> findByPerformedById(Long userId);

    List<AuditLog> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);


    
    // Find all audit logs for a specific task
    List<AuditLog> findByTaskOrderByTimestampDesc(Task task);
    
    // Find all audit logs by a specific user
    List<AuditLog> findByPerformedByOrderByTimestampDesc(User user);

    // Find audit logs within date range
    @Query("SELECT a FROM AuditLog a WHERE a.timestamp BETWEEN :startDate AND :endDate ORDER BY a.timestamp DESC")
    List<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
    
    // Find audit logs by task and date range (for filtering)
    @Query("SELECT a FROM AuditLog a WHERE a.task = :task AND a.timestamp BETWEEN :startDate AND :endDate ORDER BY a.timestamp DESC")
    List<AuditLog> findByTaskAndDateRange(@Param("task") Task task,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);
    
    // Get recent audit logs (for admin dashboard)
    List<AuditLog> findTop10ByOrderByTimestampDesc();


   
}
