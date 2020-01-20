package idea.model.dto;

import javax.validation.constraints.NotBlank;
import idea.model.validation.constraint.EmailOrPhone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
