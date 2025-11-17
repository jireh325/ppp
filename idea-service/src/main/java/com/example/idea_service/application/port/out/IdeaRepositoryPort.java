package com.example.idea_service.application.port.out;

import com.example.idea_service.domain.model.Idea;

public interface IdeaRepositoryPort {
  Idea save(Idea idea);
}
