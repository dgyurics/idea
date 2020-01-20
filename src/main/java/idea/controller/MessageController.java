package idea.controller;

import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import idea.model.entity.Message;
import idea.model.dto.MessageRequestModel;
import idea.service.MessageService;

@RestController
@RequestMapping("message")
public class MessageController {
  MessageService service;

  MessageController(MessageService service) {
    this.service = service;
  }

  @GetMapping("/{topicId}")
  public Collection<Message> getMessages(@PathVariable long topicId) {
    return service.getAllMessages(topicId);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PutMapping("/{topicId}")
  @PreAuthorize("hasRole('ROLE_USER')")  
  public Message createMessage(@PathVariable long topicId, @RequestBody MessageRequestModel message) {
    return service.createMessage(message, topicId);
  }

  @DeleteMapping("/{messageId}")
  @PreAuthorize("hasRole('ROLE_USER')")  
  public void deleteMessage(@PathVariable long messageId) {
    service.deleteMessage(messageId);
  }
}
