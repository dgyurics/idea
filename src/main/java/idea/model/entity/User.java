package idea.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_user")
public class User {
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique=true)
  private String username;

  @Column(unique=true)
  private String nickname;

  @Column
  private String mobileNumber;

  @ToString.Exclude
  @Column(nullable = false, length = 60)
  private String password;

  @Column(nullable = false)
  private boolean active;

  @Column(nullable = false)
  private String role;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createTimestamp;

  @Column
  @UpdateTimestamp
  private Date lastUpdateTimestamp;

  public User(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.nickname = user.getNickname();
    this.mobileNumber = user.getMobileNumber();
    this.password = user.getPassword();
    this.active = user.isActive();
    this.role = user.getRole();
  }
}
