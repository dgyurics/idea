package idea.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import idea.model.dto.ContactUsRequestModel;
import idea.service.ContactService;

@RestController
@RequestMapping("contact")
@RequiredArgsConstructor
public class ContactController {
  private final ContactService service;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public void createMessage(@RequestBody @Validated ContactUsRequestModel message, HttpServletRequest request) {
    message.setRemoteAddr(request.getRemoteAddr());
    service.sendMessage(message);
  }
}
