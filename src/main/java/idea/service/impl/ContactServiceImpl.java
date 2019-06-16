package idea.service.impl;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import idea.client.HttpClient;
import idea.client.model.CaptchaRequest;
import idea.client.model.CaptchaResponse;
import idea.model.request.ContactUsRequestModel;
import idea.service.ContactService;
import idea.service.EmailService;

@Service
public class ContactServiceImpl implements ContactService {
  private final HttpClient httpClient;
  private final EmailService emailService;
  private final String captchaSecret;

  ContactServiceImpl(HttpClient httpClient, EmailService emailService, 
      @Value("${captcha.secret}") String captchaSecret) {
    this.httpClient = httpClient;
    this.emailService = emailService;
    this.captchaSecret = captchaSecret;
  }

  @Override
  public void sendMessage(ContactUsRequestModel message) {
    emailService.forwardContactUsMessage(message);
  }

  @Override
  public void sendMessage(ContactUsRequestModel message, String reCaptchaResponse, String remoteAddr) {
    final CaptchaResponse response = httpClient.validateCaptcha(new CaptchaRequest(reCaptchaResponse, remoteAddr, captchaSecret));
    if(response.isSuccess()) {
      emailService.forwardContactUsMessage(message);
    } else {
      throw new WebApplicationException("Error validating reCaptcha", Response.Status.BAD_REQUEST);
    }
  }
}
