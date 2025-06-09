package com.andersen.meetup.web.communication.clients;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class GeminiClient {

  private VertexAiGeminiChatModel chatModel;

  public String getChatResponse(String value) {
    UserMessage input = new UserMessage(value);
    UserMessage limit = new UserMessage("Limit the output to 2 sentences");

    return chatModel.call(new Prompt(List.of(input, limit)))
        .getResult().getOutput().getText();
  }

  public Flux<String> getChatStream(String value) {
    UserMessage input = new UserMessage(value);
    UserMessage limit = new UserMessage("Limit the output to 2 sentences");

    return chatModel.stream(new Prompt(List.of(input, limit)))
        .map(chatResponse -> chatResponse.getResult().getOutput().getText());
  }
}
