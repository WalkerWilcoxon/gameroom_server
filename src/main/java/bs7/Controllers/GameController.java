package bs7.Controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.SocketUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import bs7.DAOs.GameFound;
import bs7.DAOs.GameInvite;
import bs7.Entities.Game;
import bs7.Entities.Move;
import bs7.Entities.Notification;
import bs7.Entities.Notification.Type;
import bs7.Entities.User;
import bs7.Repository.GameRepository;
import bs7.Repository.MoveRepository;
import bs7.Repository.UserRepository;
@Controller
@RestController
public class GameController {

  @Autowired
  private SimpMessagingTemplate template;
  private UserRepository userRepository;
  private GameRepository gameRepository;
  private MoveRepository moveRepository;
  @Autowired
  public GameController(UserRepository userRepository, GameRepository gameRepository, MoveRepository moveRepository){
    this.userRepository = userRepository;
    this.gameRepository = gameRepository;
    this.moveRepository = moveRepository;
  }

  /**
   * @param id the id of a player that wants to play
   * @param game A string of what game they would like to play
   */
  @MessageMapping("findGame/{id}/{game}")
  public void findGame(@DestinationVariable int id, @DestinationVariable String game){
    System.out.println(id + "is looking for a game of: " + game);
    findGameCoord(id, game);
  }
  /**
   * @param user1 One of the two users that wants to play the game
   * @param user2 The other of the users
   * @param game The id of the game so that they may join the same game
   */
  public void sendGameNotifications(int user1, int user2, String game, List<Move> moves){
    System.out.println(user1 + " and " + user2);
    int randomNum = (int)(Math.random()*2);
    if(randomNum == 0){ //randomly choose player1
      int temp = user1;
      user1 = user2;
      user2 = temp;
    } 
    User player1 = userRepository.findById(user1).get();
    User player2 = userRepository.findById(user2).get();
    Game newGame = new Game(game,player1, player2);
    newGame = gameRepository.save(newGame);
    GameFound gameFound = new GameFound(newGame);
    currentGames.put(newGame.getId(), 1);
    playerToGame.put(user1, newGame.getId());
    playerToGame.put(user2, newGame.getId());
    Notification notification = new Notification(Notification.Type.GAMEFOUND, gameFound);
    template.convertAndSend("/topic/notification/" + user1, notification);
    template.convertAndSend("/topic/notification/" + user2, notification);
  }
  /**
   * @param gameId The id of the game that the move is a part of
   * @param message The move the player wants to do
   * @throws Exception if there is an error sending the information to the client
   */
  @MessageMapping("game/{gameId}")
  public void gameMove(@DestinationVariable String gameId, String message) throws Exception {
    System.out.println(message);
    template.convertAndSend("/topic/game/" + gameId, message);
    moveRepository.save(new Move(new Game(Integer.parseInt(gameId)),currentGames.get(Integer.parseInt(gameId)),message));
    currentGames.put(Integer.parseInt(gameId),currentGames.get(Integer.parseInt(gameId))+1);
  }
  @MessageMapping("invite/{game}/{senderId}/{receiverId}")
  public void invite(@DestinationVariable String game, @DestinationVariable int senderId,@DestinationVariable int receiverId, String message){
    System.out.println("Sending game invitation to: " + receiverId);
    User sender = userRepository.findById(senderId).get();
    Notification notification = new Notification(Type.GAMEINVITE, new GameInvite(sender,game));
    template.convertAndSend("/topic/notification/" + receiverId, notification);
    System.out.println("Game invitation sent");
  }
  @MessageMapping("acceptInvite/{game}/{senderId}/{receiverId}")
  public void acceptInvite(@DestinationVariable String game, @DestinationVariable int senderId,@DestinationVariable int receiverId, String message){
    System.out.println("Accepting game invitation from: " + senderId);
    this.sendGameNotifications(senderId, receiverId, game, new ArrayList<Move>());
    System.out.println("Game invitation accepted");
  }
  @MessageMapping("spectate/{spectatorId}/{watchedId}")
  public void spectate(@DestinationVariable int spectatorId,@DestinationVariable int watchedId, String message){
    Game game = gameRepository.findById(playerToGame.get(watchedId)).get();
    System.out.println(game.getPlayer1()+ " " + game.getPlayer2());
    GameFound gameFound = new GameFound(game);
    System.out.println(game.getMoves().size() + "moves have been played");
    Notification notification = new Notification(Type.GAMEFOUND, gameFound);
    template.convertAndSend("/topic/notification/" + spectatorId, notification);
  }
  @PostMapping("game/winner/{gameId}/{id}")
  public void setWinner(@PathVariable String gameId, @PathVariable int id){
    if(currentGames.containsKey(gameId)){
      currentGames.remove(gameId);
    }
    Game game = gameRepository.findById(Integer.parseInt(gameId)).get();
    if(playerToGame.containsKey(game.getPlayer1().getId())){
      playerToGame.remove(game.getPlayer1().getId());
      playerToGame.remove(game.getPlayer2().getId());
    }
    game.setCompleted(true);
    if(id > 0){
      game.setWinner(new User(id));
    }
    gameRepository.save(game);
  }
  @GetMapping("gameHistory/{id}")
  public List<Game> getGameHistory(@PathVariable int id){
    List<Game> list = gameRepository.findAllByCompletedTrueAndPlayer2_id(id);
    list.addAll(gameRepository.findAllByCompletedTrueAndPlayer1_id(id));
    Collections.sort(list);
    return list;
  }
  
  @GetMapping("game/{id}")
  public ResponseEntity<List<Move>> getGameById(@PathVariable("id") Integer id){
    return new ResponseEntity<List<Move>>(gameRepository.findById(id).get().getMoves(),HttpStatus.OK);
  }
  
  public static HashMap<String, Integer> games = new HashMap<>();
  public static HashMap<Integer, Integer> currentGames = new HashMap<>();
  public static HashMap<Integer, Integer> playerToGame = new HashMap<>();
  
  public void findGameCoord(int userId, String game){
    if(!games.containsKey(game)){
      games.put(game, userId);
    }
    else {
      if(games.get(game) != userId){
        int player1 = games.get(game);
        sendGameNotifications(player1, userId, game, new ArrayList<Move>());
        games.remove(game);
      }
    }
  }
}
