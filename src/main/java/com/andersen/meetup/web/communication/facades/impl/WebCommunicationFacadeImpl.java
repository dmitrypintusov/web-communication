package com.andersen.meetup.web.communication.facades.impl;

import com.andersen.meetup.web.communication.clients.GeminiClient;
import com.andersen.meetup.web.communication.dtos.WebCommunicationResponse;
import com.andersen.meetup.web.communication.facades.WebCommunicationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class WebCommunicationFacadeImpl implements WebCommunicationFacade {

  private GeminiClient geminiClient;

  @Override
  public WebCommunicationResponse generateText(String value) {
    String chatResponse = geminiClient.getChatResponse(value);
    return new WebCommunicationResponse(chatResponse);
  }

  @Override
  public Flux<WebCommunicationResponse> generateTextStream(String value) {
    return geminiClient.getChatStream(value).map(WebCommunicationResponse::new);
  }
}
