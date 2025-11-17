package com.example.moderation_service.infrastructure.adapter.out;

import com.example.moderation_service.application.port.out.ModeratorRequestRepositoryPort;
import com.example.moderation_service.domain.model.ModeratorRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Component
public class ModeratorRequestInMemoryAdapter implements ModeratorRequestRepositoryPort {

  private final ConcurrentHashMap<String, ModeratorRequest> store = new ConcurrentHashMap<>();

  @Override
  public ModeratorRequest save(ModeratorRequest req) {
    req.setId(UUID.randomUUID().toString());
    store.put(req.getId(), req);
    return req;
  }
}
