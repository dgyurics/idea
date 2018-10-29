package idea.repository;

import org.springframework.data.repository.CrudRepository;
import idea.model.entity.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
  Iterable<Message> findAllByTopicId(Long id);
}
