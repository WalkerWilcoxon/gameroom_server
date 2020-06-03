package bs7.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name= "moves")
public class Move {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;
  @ManyToOne
  @JoinColumn(name ="game_id")
  private Game game;
  private int turn;
  private String move;
  
  public Move(Game game, int turn, String move) {
    super();
    this.game = game;
    this.turn = turn;
    this.move = move;
  }

  public Move() {
    super();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @JsonIgnore
  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public int getTurn() {
    return turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  public String getMove() {
    return move;
  }

  public void setMove(String move) {
    this.move = move;
  }
}
