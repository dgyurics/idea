package idea.repository;

import org.springframework.data.repository.CrudRepository;
import idea.model.entity.TopicEntity;

public interface TopicRepository extends CrudRepository<TopicEntity, Long> {

}
