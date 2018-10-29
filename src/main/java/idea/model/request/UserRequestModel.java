package idea.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class UserRequestModel {
  private Long id;
  private String username;
  private String email;
  @ToString.Exclude
  private String password;
}
