package idea.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;
import idea.model.entity.MessageEntity;
import idea.model.request.MessageRequestModel;
import idea.repository.MessageRepository;
import idea.service.MessageService;

@Component
public class MessageServiceImpl implements MessageService {
  private MessageRepository repository;

  MessageServiceImpl(MessageRepository repository) {
    this.repository = repository;
  }

  @Override
  public Collection<MessageEntity> getAllMessages(long topicId) {
    List<MessageEntity> list = new ArrayList<MessageEntity>();
    repository.findAllByTopicId(topicId).forEach(list::add);
    return list;
  }

  @Override
  public void deleteMessage(long messageId) {
    repository.deleteById(messageId);
  }

  @Override
  public MessageEntity createMessage(MessageRequestModel message, long topicId) {
    MessageEntity messageEntity = new MessageEntity();
    messageEntity.setTopicId(topicId);
    messageEntity.setUserId(message.getAuthor().getId());
    messageEntity.setMessage(message.getContent());
    return repository.save(messageEntity);
  }
}
