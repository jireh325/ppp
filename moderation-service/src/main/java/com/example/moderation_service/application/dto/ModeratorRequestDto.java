package com.example.moderation_service.application.dto;

import lombok.Data;

@Data
public class ModeratorRequestDto {
  private String userId;
  private String motivation;
  private String additionalInfo;
}
