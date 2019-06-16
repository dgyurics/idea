package idea.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import idea.model.request.ContactUsRequestModel;
import idea.service.ContactService;

@CrossOrigin
@RestController
@RequestMapping("contact")
public class ContactController {

  private final ContactService service;

  ContactController(ContactService service) {
    this.service = service;
  }

  @PostMapping("/{reCaptchaResponse}")
  public void createMessage(@RequestBody @Validated ContactUsRequestModel message,
      @PathVariable String reCaptchaResponse, HttpServletRequest request) {
    service.sendMessage(message, reCaptchaResponse, request.getRemoteAddr());
  }
}
