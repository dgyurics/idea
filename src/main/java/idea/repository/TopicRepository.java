package idea.repository;

import org.springframework.data.repository.CrudRepository;
import idea.model.entity.Topic;

public interface TopicRepository extends CrudRepository<Topic, Long> {

}
