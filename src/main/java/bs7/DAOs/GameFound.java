package bs7.DAOs;

import java.util.List;

import bs7.Entities.Game;
import bs7.Entities.Move;
import bs7.Entities.User;

public class GameFound {

  private User player1;
  private User player2;
  private String gameId;
  private String game;
  private List<Move> moves;
  
  public GameFound() {
    super();
  }
  
  public GameFound(User player1, User player2, String gameId, String game, List<Move> moves) {
    this.player1 = player1;
    this.player2 = player2;
    this.gameId = gameId;
    this.game = game;
    this.moves = moves;
  }
  
  public GameFound(Game game){
    this(game.getPlayer1(),game.getPlayer2(),game.getId() + "",game.getGame(),game.getMoves());
  }

  public User getPlayer1() {
    return player1;
  }

  public void setPlayer1(User player1) {
    this.player1 = player1;
  }
  
  public User getPlayer2() {
    return player2;
  }

  public void setPlayer2(User player2) {
    this.player2 = player2;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public String getGame() {
    return game;
  }

  public void setGame(String game) {
    this.game = game;
  }

  public List<Move> getMoves() {
    return moves;
  }

  public void setMoves(List<Move> moves) {
    this.moves = moves;
  }
  
}
