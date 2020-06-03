package bs7.GameLogic;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import bs7.Controllers.GameController;

public class GameCoordinator {

  @Autowired 
  GameController gameController;
  public static HashMap<String, Integer> games = new HashMap<>();
  public GameCoordinator(){}
  
  public void findGame(int userId, String game){
    if(!games.containsKey(game)){
      games.put(game, userId);
    }
    else {
      if(games.get(game) != userId){
        int player1 = games.get(game);
//        gameController.sendGameNotifications(player1, userId, ((int)(Math.random()*10000)) + "");
        games.remove(game);
      }
    }
  }
}
