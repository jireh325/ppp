package com.example.moderation_service.domain.model;

import lombok.Data;
@Data
public class ModeratorRequest {
  private String id;
  private String userId;
  private String motivation;
  private String status; // PENDING, ACCEPTED, REJECTED
}
