package com.example.moderation_service.application.service;

import com.example.moderation_service.application.dto.ModeratorRequestDto;
import com.example.moderation_service.application.port.in.SubmitModeratorRequestUseCase;
import com.example.moderation_service.application.port.out.ModeratorRequestRepositoryPort;
import com.example.moderation_service.domain.model.ModeratorRequest;

public class SubmitModeratorRequestService implements SubmitModeratorRequestUseCase {

  private final ModeratorRequestRepositoryPort repo;

  public SubmitModeratorRequestService(ModeratorRequestRepositoryPort repo) {
    this.repo = repo;
  }

  @Override
  public String submit(ModeratorRequestDto request) {
    ModeratorRequest mr = new ModeratorRequest();
    mr.setUserId(request.getUserId());
    mr.setMotivation(request.getMotivation());
    mr.setStatus("PENDING");
    ModeratorRequest saved = repo.save(mr);
    return saved.getId();
  }
}
