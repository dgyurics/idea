package idea.controller;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import idea.model.request.MessageRequestModel;

@RestController
@RequestMapping("message")
public class MessageController {

  private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

  @GetMapping("/{topicId}")
  public List<MessageRequestModel> getMessages(@PathVariable long topicId) {
    logger.info(Long.toString(topicId));
    return Collections.emptyList();
  }

  @PutMapping("/{topicId}")
  public void createMessage(@RequestBody MessageRequestModel message) {
    logger.info(message.toString());
  }

  @DeleteMapping("/{messageId}")
  public void deleteMessage(@PathVariable long messageId) {
    logger.info(Long.toString(messageId));
  }
}
