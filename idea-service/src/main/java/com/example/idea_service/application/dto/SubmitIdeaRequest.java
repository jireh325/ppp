package com.example.idea_service.application.dto;

import lombok.Data;
@Data
public class SubmitIdeaRequest {
  private String title;
  private String description;
  private String authorId;
}
