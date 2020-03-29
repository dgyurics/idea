package idea.config.websocket;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
  private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
  private final SimpMessageSendingOperations messagingTemplate;

  @EventListener
  public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
    logger.info("Received a new web socket subscribe event user: {}", event.getUser());
  }

  @EventListener
  public void handleWebSocketSubscribeListener(SessionUnsubscribeEvent event) {
    logger.info("Received a new web socket unsubscribe event user: {}", event.getUser());
  }

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    logger.info("Received a new web socket connection user: {}", event.getUser());
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    logger.info("User Disconnected user: {}", event.getUser());
  }
}
