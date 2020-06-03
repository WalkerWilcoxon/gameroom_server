package bs7.Entities;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name= "users")
public class User {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;
  private String username;
  private String password;
  private String email;
  private String bio;
  private String avatar;
  private String status;
  @OneToMany( mappedBy ="user1")
  private List<Friend> friends;
    
  public User(){}
  
  public User(int id) {
    this.id = id;
  }
  @JsonIgnore
  public List<Friend> getFriends() {
    return friends;
  }

  public void setFriends(List<Friend> friends) {
    this.friends = friends;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }
  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  } 
}