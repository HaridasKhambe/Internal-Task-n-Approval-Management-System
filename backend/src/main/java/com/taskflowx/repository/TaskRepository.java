package com.taskflowx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taskflowx.enums.TaskStatus;
import com.taskflowx.model.Task;
import com.taskflowx.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

    // Find tasks assigned to a specific user (Employee view)
    List<Task> findByAssignedTo(User user);

    // Find tasks by assigned user and status
    List<Task> findByAssignedToAndStatus(User user, TaskStatus status);

    List<Task> findByAssignedToId(Long userId);

    List<Task> findByAssignedToIdAndStatus(Long userId, TaskStatus status);

    List<Task> findByCreatedById(Long managerId);

    List<Task> findByCreatedByIdAndStatus(Long managerId, TaskStatus status);


    
    // Find tasks created by a specific user (Manager view)
    List<Task> findByCreatedBy(User user);

    // Find tasks by creator and status (Manager filtering)
    List<Task> findByCreatedByAndStatus(User user, TaskStatus status);

    // Find all tasks with specific status (Admin/Manager view)
    List<Task> findByStatus(TaskStatus status);

    // Count tasks by status for dashboard
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    long countByStatus(@Param("status") TaskStatus status);

    // Count tasks assigned to user
    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedTo = :user")
    long countByAssignedTo(@Param("user") User user);

    

}
