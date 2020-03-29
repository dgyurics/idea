package idea.service;

import idea.model.dto.ContactUsRequestModel;

public interface ContactService {
  public void sendMessage(ContactUsRequestModel message);
}
