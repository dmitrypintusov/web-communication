package com.andersen.meetup.web.communication.sockets;

import com.andersen.meetup.web.communication.dtos.WebCommunicationResponse;
import com.andersen.meetup.web.communication.facades.WebCommunicationFacade;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
@AllArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

  private WebCommunicationFacade webCommunicationFacade;

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
    String question = message.getPayload();
    WebCommunicationResponse response = webCommunicationFacade.generateText(question);
    session.sendMessage(new TextMessage(response.output()));
  }
}