package com.andersen.meetup.web.communication.grpc.server;


import com.andersen.meetup.web.communication.clients.GeminiClient;
import com.andersen.meetup.web.communication.proto.ChatRequest;
import com.andersen.meetup.web.communication.proto.ChatResponse;
import com.andersen.meetup.web.communication.proto.WebCommunicationGrpcServiceGrpc.WebCommunicationGrpcServiceImplBase;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class WebCommunicationGrpcServer extends WebCommunicationGrpcServiceImplBase {

  private GeminiClient geminiClient;

  @Override
  public void unaryGrpc(ChatRequest request, StreamObserver<ChatResponse> responseObserver) {
    String chatResponse = geminiClient.getChatResponse(request.getQuestion());

    responseObserver.onNext(ChatResponse.newBuilder()
        .setOutput(chatResponse)
        .build());

    responseObserver.onCompleted();
  }

  @Override
  public void serverStreamGrpc(ChatRequest request, StreamObserver<ChatResponse> responseObserver) {
    geminiClient.getChatStream(request.getQuestion())
        .doOnNext(response -> responseObserver.onNext(ChatResponse.newBuilder()
            .setOutput(response)
            .build())
        )
        .doOnError(responseObserver::onError)
        .doOnComplete(responseObserver::onCompleted)
        .subscribe();
  }

  @Override
  public StreamObserver<ChatRequest> clientStreamGrpc(
      StreamObserver<ChatResponse> responseObserver) {
    List<String> accumulatedUserMessages = new ArrayList<>();

    return new StreamObserver<>() {
      @Override
      public void onNext(ChatRequest request) {
        accumulatedUserMessages.add(request.getQuestion());
      }

      @Override
      public void onCompleted() {
        String chatResponse = geminiClient.getChatResponse(
            String.join(" ", accumulatedUserMessages));

        responseObserver.onNext(ChatResponse.newBuilder()
            .setOutput(chatResponse)
            .build());
        responseObserver.onCompleted();
      }

      @Override
      public void onError(Throwable t) {
        responseObserver.onError(t);
      }
    };
  }

  @Override
  public StreamObserver<ChatRequest> bidirectionalStreamGrpc(
      StreamObserver<ChatResponse> responseObserver) {

    return new StreamObserver<>() {
      @Override
      public void onNext(ChatRequest request) {
        geminiClient.getChatStream(request.getQuestion())
            .doOnNext(response -> responseObserver.onNext(ChatResponse.newBuilder()
                .setOutput(response)
                .build()))
            .subscribe();
      }

      @Override
      public void onError(Throwable t) {
        responseObserver.onError(t);
      }

      @Override
      public void onCompleted() {
        responseObserver.onCompleted();
      }
    };
  }
}