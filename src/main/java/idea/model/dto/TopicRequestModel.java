package idea.model.dto;

import java.util.Date;
import java.util.List;
import idea.model.entity.User;
import lombok.Data;

@Data
public class TopicRequestModel {
  private Long id;
  private String title;
  private User author;
  private List<UserRequestModel> privacy;
  private String backgroundImageUrl;
  private Date createTimestamp;
  private Date lastUpdateTimestamp;  
}
