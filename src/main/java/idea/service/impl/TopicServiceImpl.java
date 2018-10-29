package idea.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;
import idea.model.entity.Topic;
import idea.model.request.TopicRequestModel;
import idea.repository.TopicRepository;
import idea.service.TopicService;

@Component
public class TopicServiceImpl implements TopicService {
  private TopicRepository repository;

  TopicServiceImpl(TopicRepository repository) {
    this.repository = repository;
  }

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
