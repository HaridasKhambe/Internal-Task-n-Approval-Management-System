package com.taskflowx.enums;

/**
 * Defines the three user roles in the system.
 * <p>
 * ADMIN   - System administrator (creates users, views audit logs) MANAGER - Creates and manages
 * tasks, approves/rejects submissions EMPLOYEE - Executes assigned tasks, updates status
 * <p>
 * Used for: - User entity role assignment - Spring Security role-based access control -
 * Authorization checks in controllers
 */

public enum Role {
  ADMIN,
  MANAGER,
  EMPLOYEE
}
