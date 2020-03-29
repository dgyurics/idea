package idea.model.dto;

import lombok.Data;

@Data
public class MessageRequestModel {
  private Long id;
  private UserRequestModel author;
  private String content;
}
