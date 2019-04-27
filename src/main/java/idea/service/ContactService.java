package idea.service;

import idea.model.request.ContactUsRequestModel;

public interface ContactService {
  public void sendMessage(ContactUsRequestModel message);
}
