package com.example.moderation_service.application.port.in;

import com.example.moderation_service.application.dto.ModeratorRequestDto;

public interface SubmitModeratorRequestUseCase {
  String submit(ModeratorRequestDto request);
}
