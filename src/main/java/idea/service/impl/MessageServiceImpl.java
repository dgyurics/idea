package idea.service.impl;

import java.util.Collection;
import idea.model.entity.Message;
import idea.model.dto.MessageRequestModel;
import idea.repository.MessageRepository;
import idea.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
  private final MessageRepository repository;

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
