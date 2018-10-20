package idea.controller;

import java.util.Collection;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import idea.model.entity.MessageEntity;
import idea.model.request.MessageRequestModel;
import idea.service.MessageService;

@RestController
@RequestMapping("message")
public class MessageController {
  MessageService service;

  MessageController(MessageService service) {
    this.service = service;
  }

  @GetMapping("/{topicId}")
  public Collection<MessageEntity> getMessages(@PathVariable long topicId) {
    return service.getAllMessages(topicId);
  }

  @PutMapping("/{topicId}")
  public MessageEntity createMessage(@PathVariable long topicId, @RequestBody MessageRequestModel message) {
    return service.createMessage(message, topicId);
  }

  @DeleteMapping("/{messageId}")
  public void deleteMessage(@PathVariable long messageId) {
    service.deleteMessage(messageId);
  }
}
