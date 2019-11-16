package idea.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import idea.model.entity.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
  List<Message> findAllByTopicId(Long id);
}
