package idea.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import idea.model.validation.group.NewUserGroup;
import idea.model.validation.group.PasswordConfirmationGroup;
import idea.model.validation.group.PasswordRequestGroup;
import idea.model.validation.group.RemoveUserGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class UserRequestModel {
  private Long id;

  @Size(min = 3,max = 30, groups = {NewUserGroup.class, RemoveUserGroup.class})
  @NotEmpty(groups = {NewUserGroup.class, RemoveUserGroup.class, PasswordConfirmationGroup.class})
  private String username;

  @NotEmpty(groups = {PasswordRequestGroup.class})
  @Email(groups = {NewUserGroup.class, RemoveUserGroup.class, PasswordRequestGroup.class})
  private String email;

  @ToString.Exclude
  @Size(min = 5,max = 100, groups = {NewUserGroup.class, RemoveUserGroup.class})
  @NotEmpty(groups = {NewUserGroup.class, RemoveUserGroup.class})
  private String password;

  @ToString.Exclude
  @Size(min = 5,max = 5, groups = {PasswordConfirmationGroup.class})
  private int resetCode;
}
