package bs7.Entities;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name= "friends")
public class Friend {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;
  @ManyToOne
  @JoinColumn(name ="user1")
  private User user1;
  @ManyToOne
  @JoinColumn(name ="user2")
  private User user2;
  private boolean confirmed;
  
  public Friend(){}

  public Friend(User user1, User user2, boolean confirmed) {
    this.user1 = user1;
    this.user2 = user2;
    this.confirmed = confirmed;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
  @JsonIgnore
  public User getUser1() {
    return user1;
  }

  public void setUser1(User user1) {
    this.user1 = user1;
  }

  @JsonIgnore
  public User getUser2() {
    return user2;
  }

  public void setUser2(User user2) {
    this.user2 = user2;
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

  
}
