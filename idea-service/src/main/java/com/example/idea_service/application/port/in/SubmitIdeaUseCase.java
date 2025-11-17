package com.example.idea_service.application.port.in;

import com.example.idea_service.application.dto.SubmitIdeaRequest;

public interface SubmitIdeaUseCase {
  String submit(SubmitIdeaRequest request);
}
