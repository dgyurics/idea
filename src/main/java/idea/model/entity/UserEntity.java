package idea.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@Entity(name = "user")
public class UserEntity {
  @Id
  @GeneratedValue
  private Long id;
  @Column(nullable = false)
  private String username;
  private String nickname;
  private String mobileNumber;
  @Column(length = 100, nullable = false)
  private String email;
  @ToString.Exclude
  @Column(nullable = false)
  private String encryptedPassword;
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createTimestamp;
  private Date lastUpdateTimestamp;
}
