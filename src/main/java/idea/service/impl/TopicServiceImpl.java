package idea.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import idea.model.entity.Topic;
import idea.model.dto.TopicRequestModel;
import idea.repository.TopicRepository;
import idea.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
  private final TopicRepository repository;

  @Override
  public Collection<Topic> getAllTopics() {
    List<Topic> list = new ArrayList<Topic>();
    repository.findAll().forEach(list::add);
    return list;
  }

  @Override
  public Topic createTopic(TopicRequestModel topic) {
    Topic topicEntity = new Topic();
    topicEntity.setName(topic.getTitle());
    topicEntity.setBackgroundImageUrl(topic.getBackgroundImageUrl());
    topicEntity.setUserId(1234L);
    return repository.save(topicEntity);
  }
}
