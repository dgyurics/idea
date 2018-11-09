package idea.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class RegistrationRequestModel {
  @NotEmpty
  @Size(min = 3,max = 30)
  private String username;
  @Email
  private String email;
  @NotEmpty
  @ToString.Exclude
  @Size(min = 5,max = 100)
  private String password;
}
