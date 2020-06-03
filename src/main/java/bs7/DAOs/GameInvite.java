package bs7.DAOs;

import bs7.Entities.User;

public class GameInvite {

  private User inviter;
  private String game;
  public GameInvite(User inviter, String game) {
    super();
    this.inviter = inviter;
    this.game = game;
  }
  public User getSender() {
    return inviter;
  }
  public void setSender(User sender) {
    this.inviter = sender;
  }
  public String getGame() {
    return game;
  }
  public void setGame(String game) {
    this.game = game;
  }
}
