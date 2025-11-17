package com.example.idea_service.domain.model;

import lombok.Data;
@Data
public class Idea {
  private String id;
  private String title;
  private String description;
  private String authorId;
}
