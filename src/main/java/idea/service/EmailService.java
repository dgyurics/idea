package idea.service;

import org.springframework.mail.SimpleMailMessage;
import idea.model.dto.ContactUsRequestModel;

public interface EmailService {
  void send(SimpleMailMessage message);
  void forwardContactUsMessage(ContactUsRequestModel message);
  void sendResetCode(String to, String userId, int code);
}
