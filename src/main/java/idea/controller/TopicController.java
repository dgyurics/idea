package idea.controller;

import java.util.Collections;
import java.util.List;
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
  @GetMapping
  public List<TopicRequestModel> getTopics() {
    return Collections.emptyList();
  }

  @PutMapping
  public void createTopic(@RequestBody TopicRequestModel topic) {
  }

  @DeleteMapping("/{id}")
  public void deleteTopic(@PathVariable long id) {
  }
}
