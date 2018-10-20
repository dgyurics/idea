package idea.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class MessageRequestModel {
  private Long id;
  private UserRequestModel author;
  private String content;
}
