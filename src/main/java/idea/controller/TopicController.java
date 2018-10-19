package idea.controller;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import idea.model.request.TopicRequestModel;

@RestController
@RequestMapping("topic")
public class TopicController {

  private static final Logger logger = LoggerFactory.getLogger(TopicController.class);

  @GetMapping
  public List<TopicRequestModel> getTopics() {
    logger.info("get topics");
    return Collections.emptyList();
  }

  @PutMapping
  public void createTopic(@RequestBody TopicRequestModel topic) {
    logger.info(topic.toString());
  }

  @DeleteMapping("/{id}")
  public void deleteTopic(@PathVariable long id) {
    logger.info(Long.toString(id));
  }
}
