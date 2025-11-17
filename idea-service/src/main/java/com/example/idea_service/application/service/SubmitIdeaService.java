package com.example.idea_service.application.service;

import com.example.idea_service.application.dto.SubmitIdeaRequest;
import com.example.idea_service.application.port.in.SubmitIdeaUseCase;
import com.example.idea_service.application.port.out.IdeaRepositoryPort;
import com.example.idea_service.domain.model.Idea;

public class SubmitIdeaService implements SubmitIdeaUseCase {

  private final IdeaRepositoryPort repo;

  public SubmitIdeaService(IdeaRepositoryPort repo) {
    this.repo = repo;
  }

  @Override
  public String submit(SubmitIdeaRequest request) {
    Idea i = new Idea();
    i.setTitle(request.getTitle());
    i.setDescription(request.getDescription());
    i.setAuthorId(request.getAuthorId());
    Idea saved = repo.save(i);
    return saved.getId();
  }
}
