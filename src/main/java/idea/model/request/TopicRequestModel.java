package idea.model.request;

import java.util.Date;
import java.util.List;
import idea.model.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class TopicRequestModel {
  private Long id;
  private String title;
  private UserEntity author;
  private List<UserRequestModel> privacy;
  private String backgroundImageUrl;
  private Date createTimestamp;
  private Date lastUpdateTimestamp;  
}
