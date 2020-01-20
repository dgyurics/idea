package idea.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import idea.client.HttpClientNonBlock;
import idea.client.model.CaptchaRequest;
import idea.model.dto.ContactUsRequestModel;
import idea.service.ContactService;
import idea.service.EmailService;

@Service
public class ContactServiceImpl implements ContactService {
  private Logger logger = LoggerFactory.getLogger(ContactService.class);
  private final HttpClientNonBlock httpClient;
  private final EmailService emailService;
  private final String captchaSecret;

  ContactServiceImpl(HttpClientNonBlock httpClient, EmailService emailService, 
      @Value("${captcha.secret}") String captchaSecret) {
    this.httpClient = httpClient;
    this.emailService = emailService;
    this.captchaSecret = captchaSecret;
  }

  @Async
  @Override
  public void sendMessage(ContactUsRequestModel message) {
    logger.info("New contact us message received. Payload: {}", message);
    // NOTE: not enforcing captcha yet
    httpClient.validateCaptcha(new CaptchaRequest(message.getReCaptchaResponse(), message.getRemoteAddr(), captchaSecret));
    emailService.forwardContactUsMessage(message);
  }
}
