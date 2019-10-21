package idea.controller;

import idea.model.Notification;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {
  private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

  @MessageMapping("/create")
  @SendTo("/notifications")
  public Notification createNotification(String content) throws InterruptedException {
    return new Notification(Objects.toString(content, ""));
  }
}
