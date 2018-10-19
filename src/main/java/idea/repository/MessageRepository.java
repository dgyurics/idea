package idea.repository;

import org.springframework.data.repository.CrudRepository;
import idea.model.entity.MessageEntity;

public interface MessageRepository extends CrudRepository<MessageEntity, Long> {

}
