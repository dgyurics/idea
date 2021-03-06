package idea.controller;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import idea.model.entity.Topic;
import idea.model.dto.TopicRequestModel;
import idea.service.TopicService;

@RestController
@RequestMapping("topic")
@RequiredArgsConstructor
public class TopicController {
  private final TopicService service;

  @GetMapping
  public Collection<Topic> getTopics() {
    return service.getAllTopics();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PutMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<Topic> createTopic(@RequestBody TopicRequestModel topic) {
    return new ResponseEntity<Topic>(service.createTopic(topic), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_USER')")
  public void deleteTopic(@PathVariable long id) {
  }
}
