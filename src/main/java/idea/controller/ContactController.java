package idea.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import idea.model.request.ContactUsRequestModel;
import idea.service.ContactService;

@RestController
@RequestMapping("contact")
public class ContactController {

  private final ContactService service;

  ContactController(ContactService service) {
    this.service = service;
  }

  @PostMapping
  public void createMessage(@RequestBody @Validated ContactUsRequestModel message, HttpServletRequest request) {
    message.setRemoteAddr(request.getRemoteAddr());
    service.sendMessage(message);
  }
}
