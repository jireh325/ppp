package com.example.identity_profile_service.application.port.out;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AuditPort {

    void logUserAction(UUID userId, String action, String resource, String details);
    void logAdminAction(UUID adminId, String action, UUID targetUserId, String details);
    void logRoleChange(UUID changedBy, UUID targetUserId, String fromRole, String toRole, String reason);
    void logSecurityEvent(String eventType, UUID userId, String details, String severity);
}