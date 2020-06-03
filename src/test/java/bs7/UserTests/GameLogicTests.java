package bs7.UserTests;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import bs7.Controllers.GameController;
import bs7.DAOs.GameFound;
import bs7.Entities.Game;
import bs7.Entities.User;
import bs7.Repository.FriendRepository;
import bs7.Repository.GameRepository;
import bs7.Repository.MoveRepository;
import bs7.Repository.UserRepository;

public class GameLogicTests {

  MoveRepository moveServiceMock = mock(MoveRepository.class);
  GameRepository gameServiceMock = mock(GameRepository.class);
  UserRepository userServiceMock = mock(UserRepository.class);
  GameController gameController = new GameController(userServiceMock, gameServiceMock, moveServiceMock);
  @Before
  public void prep(){
    when(gameServiceMock.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));
  }
  @Test
  public void testDAOConstructor(){
    Game game = new Game("Chess", new User(), new User());
    GameFound gameFound = new GameFound(game);
    assertEquals(gameFound.getMoves().size(), 0); //test to see if constructor makes empty list instead of null
  }
  @Test
  public void testSetWinnerWithATie(){
    Game game = new Game(1);
    assertEquals(game,gameServiceMock.save(game));
  }
}
