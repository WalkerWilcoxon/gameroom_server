package bs7.UserTests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import bs7.Controllers.UserController;
import bs7.Entities.User;
import bs7.GameLogic.GameCoordinator;
import bs7.Repository.FriendRepository;
import bs7.Repository.UserRepository;

public class UserTests {

  UserRepository userServiceMock = mock(UserRepository.class);
  FriendRepository friendServiceMock = mock(FriendRepository.class);
  GameCoordinator gameCoordinator = new GameCoordinator();
  GameCoordinator gameCoordinator2 = new GameCoordinator();
  UserController userController = new UserController(userServiceMock, friendServiceMock);
  @Before
  public void prep(){
    List<User> nonEmptyListofUser = new ArrayList<User>();
    nonEmptyListofUser.add(new User(100));
    when(userServiceMock.findByUsername("alreadyTaken")).thenReturn(nonEmptyListofUser);
    when(userServiceMock.findByUsername("notTaken")).thenReturn(new ArrayList<User>());
    when(userServiceMock.findOneByUsernameAndPassword("test", "password123")).thenReturn(new User(10));
    List<User> list = new ArrayList<>();
    list.add(new User(1));
    list.add(new User(2));
    when(userServiceMock.findAllByUsernameContains("username")).thenReturn(list);
  }
  @Test
  public void testLogin() {
    assertEquals(new Integer(10), userServiceMock.findOneByUsernameAndPassword("test", "password123").getId());
    assertEquals(null, userServiceMock.findOneByUsernameAndPassword("test2", "password123"));
  }
  @Test
  public void testSearch(){
    assertEquals(2, userServiceMock.findAllByUsernameContains("username").size());
    assertEquals(0, userServiceMock.findAllByUsernameContains("username2").size());
  }
  
  @Test 
  public void testGameCoordination(){
    gameCoordinator.findGame(1, "checkers");
    gameCoordinator.findGame(1, "checkers");
    assert(GameCoordinator.games.get("checkers") == 1);// check that each can only enter queue once
    gameCoordinator.findGame(2, "chess");
    assert(GameCoordinator.games.keySet().size() == 2);
    gameCoordinator.findGame(3, "checkers");
    assert(GameCoordinator.games.keySet().size() == 1);
    gameCoordinator.findGame(4, "chess");
    assert(GameCoordinator.games.keySet().size() == 0);
    
  }
  @Test
  public void testSignUpWithRepeatedUsername() throws Exception{
    User repeatedUser = new User();
    repeatedUser.setUsername("alreadyTaken");
    assert(userController.signUp(repeatedUser).getStatusCode().equals( HttpStatus.BAD_REQUEST));
  }
  @Test
  public void testSignUpWithUniqueUsername() throws Exception{
    User repeatedUser = new User();
    repeatedUser.setUsername("notTaken");
    assert(userController.signUp(repeatedUser).getStatusCode().equals( HttpStatus.OK));
  }

}