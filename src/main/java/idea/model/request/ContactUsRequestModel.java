package idea.model.request;

import javax.validation.constraints.NotBlank;
import idea.model.validation.constraint.EmailOrPhone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class ContactUsRequestModel {
  @EmailOrPhone
  @NotBlank
  private String contactInfo;
  @NotBlank
  private String message;
  private String reCaptchaResponse;
  private String remoteAddr;
}
