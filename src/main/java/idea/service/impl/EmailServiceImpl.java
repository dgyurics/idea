package idea.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import idea.model.request.ContactUsRequestModel;
import idea.service.EmailService;

@Component
public class EmailServiceImpl implements EmailService {
  private final JavaMailSender emailSender;

  EmailServiceImpl(JavaMailSender emailSender) {
    this.emailSender = emailSender;
  }

  @Override
  public void forwardContactUsMessage(ContactUsRequestModel message) {
    final String text = String.format("Contact info: %s \n%s", message.getContactInfo(), message.getMessage());
    final SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo("denxnis@gmail.com"); // move this to configuration file
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
    final String text = String.format("Your reset code is %s. Enter at https://lagom.life/reset-password/%s", code, userId);
    final SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("Password reset");
    message.setText(text);
    send(message);
  }
}
