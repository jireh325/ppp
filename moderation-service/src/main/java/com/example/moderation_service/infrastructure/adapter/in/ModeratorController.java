package com.example.moderation_service.infrastructure.adapter.in;

import com.example.moderation_service.application.dto.ModeratorRequestDto;
import com.example.moderation_service.application.port.in.SubmitModeratorRequestUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moderator-requests")
public class ModeratorController {

  private final SubmitModeratorRequestUseCase submitUseCase;

  public ModeratorController(SubmitModeratorRequestUseCase submitUseCase) {
    this.submitUseCase = submitUseCase;
  }

  @PostMapping
  public String submit(@RequestBody ModeratorRequestDto req) {
    return submitUseCase.submit(req);
  }
}
