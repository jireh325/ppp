package com.example.idea_service.infrastructure.adapter.in;

import com.example.idea_service.application.dto.SubmitIdeaRequest;
import com.example.idea_service.application.port.in.SubmitIdeaUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ideas")
public class IdeaController {

  private final SubmitIdeaUseCase submitIdeaUseCase;

  public IdeaController(SubmitIdeaUseCase submitIdeaUseCase) {
    this.submitIdeaUseCase = submitIdeaUseCase;
  }

  @PostMapping
  public String submit(@RequestBody SubmitIdeaRequest req) {
    return submitIdeaUseCase.submit(req);
  }
}
