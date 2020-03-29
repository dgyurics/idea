package idea.model.dto;

import idea.model.validation.group.AuthenticateGroup;
import idea.model.validation.group.ResetCodeValidationGroup;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import idea.model.validation.group.NewUserGroup;
import idea.model.validation.group.PasswordConfirmationGroup;
import idea.model.validation.group.PasswordRequestGroup;
import idea.model.validation.group.RemoveUserGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class UserRequestModel {
  private Long id;

  @NotEmpty(groups = {AuthenticateGroup.class, NewUserGroup.class, RemoveUserGroup.class, PasswordRequestGroup.class})
  @Email(groups = {AuthenticateGroup.class, NewUserGroup.class, RemoveUserGroup.class, PasswordRequestGroup.class},
      message="Username must be a valid email")
  private String username;

  @ToString.Exclude
  @Size(min = 5, groups = {AuthenticateGroup.class, NewUserGroup.class, RemoveUserGroup.class, PasswordConfirmationGroup.class},
      message="Password must be at least five characters long")
  @Size(max = 50, groups = {AuthenticateGroup.class, NewUserGroup.class, RemoveUserGroup.class, PasswordConfirmationGroup.class},
      message="Password must be at least five characters long")
  @NotEmpty(groups = {AuthenticateGroup.class, NewUserGroup.class, RemoveUserGroup.class, PasswordConfirmationGroup.class})
  private String password;

  @ToString.Exclude
  @Min(value = 100000, groups = {PasswordConfirmationGroup.class, ResetCodeValidationGroup.class}, message="Reset code must be six digits")
  @Max(value = 999999, groups = {PasswordConfirmationGroup.class, ResetCodeValidationGroup.class}, message="Reset code must be six digits")
  private int resetCode;

  @ToString.Exclude
  //@Min(value = 100000, groups = {NewUserGroup.class})
  //@Max(value = 999999, groups = {NewUserGroup.class})
  private int registrationCode;
}
