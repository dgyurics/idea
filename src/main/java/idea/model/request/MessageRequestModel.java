package idea.model.request;

import lombok.Data;

@Data
public class MessageRequestModel {
  private Long id;
  private UserRequestModel author;
  private String content;
}
