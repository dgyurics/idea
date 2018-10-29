package idea.service;

import java.util.Collection;
import idea.model.entity.Topic;
import idea.model.request.TopicRequestModel;

public interface TopicService {
  Collection<Topic> getAllTopics();
  Topic createTopic(TopicRequestModel topic);
}
