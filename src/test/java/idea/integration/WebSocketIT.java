package idea.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;

import idea.model.Notification;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class WebSocketIT extends BaseIT {
  private String URL;
  private CompletableFuture<Notification> completableFuture;
  private static final String SEND_GREETING_ENDPOINT = "/app/create";
  private static final String SUBSCRIBE_ENDPOINT = "/notifications";

  @Before
  public void setup() {
    completableFuture = new CompletableFuture<>();
    URL = "ws://localhost:" + port + "/notify";
  }

  @Test
  public void subscribeAndEmitMessage()
      throws InterruptedException, TimeoutException, java.util.concurrent.ExecutionException {
    WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {}).get(1, SECONDS);
    stompSession.subscribe(SUBSCRIBE_ENDPOINT, new CreateFrameHandler());
    stompSession.send(SEND_GREETING_ENDPOINT, "a message");
    assertNotNull(completableFuture.get(10, SECONDS));
  }

  private List<Transport> createTransportClient() {
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
    return transports;
  }

  private class CreateFrameHandler implements StompFrameHandler {
    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
      return Notification.class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
      Notification result = (Notification) o;
      completableFuture.complete(result);
    }
  }
}
