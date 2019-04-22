package idea.model.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// User is a reserved word in many databases
@ToString
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "user_spring_security", indexes = {
    @Index(columnList = "id", name = "user_id_index"),
    @Index(columnList = "email", name = "user_email_index")
})
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

  @Column(unique=true)
  private String email;

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
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.active = user.isActive();
    this.role = user.getRole();
  }
}
