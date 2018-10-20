package idea.service;

import java.util.Collection;
import idea.model.entity.MessageEntity;
import idea.model.request.MessageRequestModel;

public interface MessageService {
  Collection<MessageEntity> getAllMessages(long topicId);
  void deleteMessage(long messageId);
  MessageEntity createMessage(MessageRequestModel message, long topicId);
}
