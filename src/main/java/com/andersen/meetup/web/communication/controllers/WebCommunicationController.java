package com.andersen.meetup.web.communication.controllers;

import com.andersen.meetup.web.communication.dtos.WebCommunicationResponse;
import com.andersen.meetup.web.communication.facades.WebCommunicationFacade;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
public class WebCommunicationController {

  private WebCommunicationFacade webCommunicationFacade;

  @GetMapping(value = "/api/rest")
  public WebCommunicationResponse getRestMessage(
      @RequestParam(value = "question") String question) {
    return webCommunicationFacade.generateText(question);
  }

  @GetMapping(value = "/api/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<WebCommunicationResponse> getSseMessage(
      @RequestParam(value = "question") String question) {
    return webCommunicationFacade.generateTextStream(question)
        .delayElements(Duration.ofSeconds(1));
  }
}
