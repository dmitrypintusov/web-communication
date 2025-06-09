package com.andersen.meetup.web.communication.facades;

import com.andersen.meetup.web.communication.dtos.WebCommunicationResponse;
import reactor.core.publisher.Flux;

public interface WebCommunicationFacade {

  WebCommunicationResponse generateText(String value);
  Flux<WebCommunicationResponse> generateTextStream(String value);
}
