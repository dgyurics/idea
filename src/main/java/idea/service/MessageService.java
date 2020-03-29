package idea.service;

import java.util.Collection;
import idea.model.entity.Message;
import idea.model.dto.MessageRequestModel;

public interface MessageService {
  Collection<Message> getAllMessages(long topicId);
  void deleteMessage(long messageId);
  Message createMessage(MessageRequestModel message, long topicId);
}
