package idea.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
  void send(SimpleMailMessage message);
  void sendResetCode(String to, String userId, int code);
}
