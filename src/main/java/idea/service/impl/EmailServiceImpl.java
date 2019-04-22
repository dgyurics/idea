package idea.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import idea.service.EmailService;

@Component
public class EmailServiceImpl implements EmailService {
  private final JavaMailSender emailSender;

  EmailServiceImpl(JavaMailSender emailSender) {
    this.emailSender = emailSender;
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
