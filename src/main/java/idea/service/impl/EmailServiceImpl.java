package idea.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import idea.model.dto.ContactUsRequestModel;
import idea.service.EmailService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
  private final JavaMailSender emailSender;

  @Override
  public void forwardContactUsMessage(ContactUsRequestModel message) {
    final String text = String.format("Contact info: %s \n%s", message.getContactInfo(), message.getMessage());
    final SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo("denxnis@gmail.com"); // TODO: move this to configuration file
    mailMessage.setSubject("lagom.life message");
    mailMessage.setText(text);
    send(mailMessage);
  }

  @Override
  public void send(SimpleMailMessage message) {
    emailSender.send(message);
  }

  // TODO move email content to configuration file
  @Override
  public void sendResetCode(final String to, final String userId, int code) {
    final String text = String.format("Your reset code is %s. Enter at https://lagom.life/authentication/%s", code, userId);
    final SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("Password reset");
    message.setText(text);
    send(message);
  }
}
