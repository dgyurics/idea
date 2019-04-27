package idea.service.impl;

import org.springframework.stereotype.Service;
import idea.model.request.ContactUsRequestModel;
import idea.service.ContactService;
import idea.service.EmailService;

@Service
public class ContactServiceImpl implements ContactService {

  private final EmailService service;

  ContactServiceImpl(EmailService service) {
    this.service = service;
  }

  @Override
  public void sendMessage(ContactUsRequestModel message) {
    service.forwardContactUsMessage(message);
  }
}
