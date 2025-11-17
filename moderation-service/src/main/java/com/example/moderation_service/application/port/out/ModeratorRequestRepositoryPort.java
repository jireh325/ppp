package com.example.moderation_service.application.port.out;

import com.example.moderation_service.domain.model.ModeratorRequest;

public interface ModeratorRequestRepositoryPort {
  ModeratorRequest save(ModeratorRequest req);
}
