package com.example.idea_service.infrastructure.adapter.out;

import com.example.idea_service.application.port.out.IdeaRepositoryPort;
import com.example.idea_service.domain.model.Idea;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdeaRepositoryInMemoryAdapter implements IdeaRepositoryPort {

  private final ConcurrentHashMap<String, Idea> store = new ConcurrentHashMap<>();

  @Override
  public Idea save(Idea idea) {
    idea.setId(UUID.randomUUID().toString());
    store.put(idea.getId(), idea);
    return idea;
  }
}
