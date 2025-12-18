package com.taskflowx.enums;
/**
 * Task workflow states
 * Flow: CREATED → ASSIGNED → IN_PROGRESS → SUBMITTED → APPROVED/REJECTED → CLOSED
 */
public enum TaskStatus {
    CREATED,
    ASSIGNED,
    IN_PROGRESS,
    SUBMITTED,
    APPROVED,
    REJECTED,
    CLOSED
}
