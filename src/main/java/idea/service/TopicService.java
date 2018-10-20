package idea.service;

import java.util.Collection;
import idea.model.entity.TopicEntity;
import idea.model.request.TopicRequestModel;

public interface TopicService {
  Collection<TopicEntity> getAllTopics();
  TopicEntity createTopic(TopicRequestModel topic);
}
