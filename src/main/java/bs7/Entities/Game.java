package bs7.Entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name= "results")
public class Game implements Comparable{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;
  private String game;

  @ManyToOne
  @JoinColumn(name ="player1")
  private User player1;
  @ManyToOne
  @JoinColumn(name ="player2")
  private User player2;
  @ManyToOne
  @JoinColumn(name ="winner")
  private User winner;
  @OneToMany( mappedBy ="game",fetch = FetchType.EAGER)
  private List<Move> moves;
  private boolean completed;
  public Game() {
    super();
  }
  
  public Game(Integer id) { //create a shell for persisting
    this.id = id;
  }

  public Game(String game, User player1, User player2) {
    this.game = game;
    this.player1 = player1;
    this.player2 = player2;
    this.moves = new ArrayList<>();
  }

  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public String getGame() {
    return game;
  }
  public void setGame(String game) {
    this.game = game;
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
  public User getWinner() {
    return winner;
  }
  public void setWinner(User winner) {
    this.winner = winner;
  }
  public List<Move> getMoves() {
    return moves;
  }
  public void setMoves(List<Move> moves) {
    this.moves = moves;
  }
  public boolean isCompleted() {
    return completed;
  }
  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  @Override
  public int compareTo(Object arg0) {
    return this.id - ((Game)arg0).getId();
  }
}
