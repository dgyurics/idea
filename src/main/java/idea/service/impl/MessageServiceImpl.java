package idea.service.impl;

import java.util.Collection;
import idea.model.entity.Message;
import idea.model.dto.MessageRequestModel;
import idea.repository.MessageRepository;
import idea.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
  private final MessageRepository repository;

  MessageServiceImpl(MessageRepository repository) {
    this.repository = repository;
  }

  @Override
  public Collection<Message> getAllMessages(long topicId) {
    return repository.findAllByTopicId(topicId);
  }

  @Override
  public void deleteMessage(long messageId) {
    repository.deleteById(messageId);
  }

  @Override
  public Message createMessage(MessageRequestModel message, long topicId) {
    Message messageEntity = new Message();
    messageEntity.setTopicId(topicId);
    messageEntity.setUserId(message.getAuthor().getId());
    messageEntity.setMessage(message.getContent());
    return repository.save(messageEntity);
  }
}
