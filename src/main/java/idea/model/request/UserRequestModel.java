package idea.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class UserRequestModel {
  private Long id;
  @NotEmpty
  @Size(min = 3,max = 30)
  private String username;
  @Email
  private String email;
  @NotEmpty
  @Size(min = 5,max = 100)
  @ToString.Exclude
  private String password;
}
