package idea.controller;

import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import idea.model.entity.TopicEntity;
import idea.model.request.TopicRequestModel;
import idea.service.TopicService;

@RestController
@RequestMapping("topic")
public class TopicController {
  TopicService service;

  TopicController(TopicService service) {
    this.service = service;
  }

  @GetMapping
  public Collection<TopicEntity> getTopics() {
    return service.getAllTopics();
  }

  @PutMapping
  public ResponseEntity<TopicEntity> createTopic(@RequestBody TopicRequestModel topic) {
    return new ResponseEntity<TopicEntity>(service.createTopic(topic), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public void deleteTopic(@PathVariable long id) {
  }
}
