package bs7.DAOs;

import bs7.Entities.User;

public class FriendUpdate {

  private User user;
  private String status;
  public FriendUpdate(User user, String status) {
    super();
    this.user = user;
    this.status = status;
  }
  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
}
