package idea.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookRequestModel {
  private String title;
  private String alt;
  private String author;
  private String src;
}
