package bs7.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import bs7.Entities.Friend;
import bs7.Entities.Notification;
import bs7.Entities.User;
import bs7.Repository.FriendRepository;
import bs7.Repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class UserController {

  private UserRepository userRepository;
  private FriendRepository friendRepository;
  @Autowired
  public UserController(UserRepository userRepository, FriendRepository friendRepository){
    this.friendRepository = friendRepository;
    this.userRepository = userRepository;
  }
  @Autowired
  private SimpMessagingTemplate template;
  /**
   * @param user The user object to be persisted
   * @return A response entity with the proper status
   * @throws Exception if there is an internal problem persisting the User
   */
  @PostMapping("/signup")
  public ResponseEntity<String> signUp(@RequestBody User user) throws Exception {
    if(!userRepository.findByUsername(user.getUsername()).isEmpty()){
      return new ResponseEntity<String>("Username already exists", HttpStatus.BAD_REQUEST);
    }
    userRepository.save(user);
    return new ResponseEntity<String>("success", HttpStatus.OK);
  }
  /**
   * @param user The User object to check username and password
   * @return A response entity with the user object if authentication is correc
   * @throws Exception if there is an internal problem with database
   */
  @PostMapping("/login")
  public ResponseEntity<User> login(@RequestBody User user) throws Exception {
    User user2 = userRepository.findOneByUsernameAndPassword(user.getUsername(), user.getPassword());
    if(user2 == null){
      return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<User>(user2, HttpStatus.OK);
  }  
  /**
   * @param user The User object that needs all their friends notified that they logined in
   */
  private void alertFriends(User user){
    Set<User> friends = getAllFriends(user.getId());
    Notification notification = new Notification(Notification.Type.FRIENDUPDATE, user);
    for(User friend: friends){
      template.convertAndSend("/topic/notification/" + friend.getId(), notification);
    }
  }

  /**
   * @param id The id of the user that a user wants to get the friends list from
   * @return A list of all friends
   */
  @GetMapping("/friends/{id}")
  public Set<User> getAllFriends(@PathVariable("id") Integer id){
    return userRepository.findById(id).get().getFriends()
        .stream()
        .filter(Friend::isConfirmed)
        .map(Friend::getUser2)
        .collect(Collectors.toSet());
  }
  
  /**
   * @param id The id of the user that wants to get all open friend requests
   * @return The list of users that have sent a friend request to the user
   */
  @GetMapping("/friends/requests/{id}")
  public Set<User> getAllRequests(@PathVariable("id") Integer id){
    return friendRepository.findAllByConfirmedFalseAndUser2_Id(id)
        .stream()
        .map(Friend::getUser1)
        .collect(Collectors.toSet());
  }
  /**
   * @param id The id of the user that wants to get their own friend requests
   * @return The list of Users that the user has sent a friend request to and is still unmatched
   */

  @GetMapping("/friends/sentRequests/{id}")
  public Set<User> getSentRequests(@PathVariable("id") Integer id){
    return friendRepository.findAllByConfirmedFalseAndUser1_Id(id)
        .stream()
        .map(Friend::getUser2)
        .collect(Collectors.toSet());
  }
  /**
   * @param string The string the user has searched to 
   * @return Returns the list of users in the database that contains the searched string, exact match comes first.
   */
  @GetMapping("/users/search/{string}")
  public List<User> friendSearch(@PathVariable("string") String string){
    List<User> users = userRepository.findAllByUsernameContains(string);
    for(int i = 0; i < users.size(); i++){
      if(users.get(i).getUsername().equals(string)){
        User temp = users.get(i);
        users.set(i, users.get(0));
        users.set(0, temp);
      }
    }
    return users;
  }  
  /**
   * @param user The user object that contains the password
   * @return A string with the success of the persisting
   */
  @PostMapping("/user/changePassword")
  public String changePassword(@RequestBody User user){
    User user2 = userRepository.findById(user.getId()).get();
    user2.setPassword(user.getPassword());
    userRepository.save(user2);
    return "success";
  }  
  /**
   * @param user The user object that contains the bio
   * @return A string with the success of the persisting
   */

  @PostMapping("/user/changeBio")
  public ResponseEntity<String> changeBio(@RequestBody User user){
    User user2 = userRepository.findById(user.getId()).get();
    user2.setBio(user.getBio());
    userRepository.save(user2);
    return new ResponseEntity<String>(HttpStatus.OK);
  }

  
  @PostMapping("/user/changeAvatar")
  public ResponseEntity<String> changeAvatar(@RequestBody User user){
    User user2 = userRepository.findById(user.getId()).get();
    user2.setAvatar(user.getAvatar());
    userRepository.save(user2);
    return new ResponseEntity<String>(HttpStatus.OK);
  }

  @PostMapping("/status/{id}/{status}")
  public ResponseEntity<String> changeStatus(@PathVariable("id") Integer id, @PathVariable("status") String status ){
    User user = userRepository.findById(id).get();
    user.setStatus(status);
    userRepository.save(user);
    alertFriends(user);
    return new ResponseEntity<String>(HttpStatus.OK);
  }
  /**
   * @param id The id of the user that wants to be retrieved
   * @return The user that wants to be retrieved
   */
  @GetMapping("/user/{id}")
  public User getUser(@PathVariable("id") Integer id){
    return userRepository.findById(id).get();
  }
  
  /**
   * @param id The id of the user sending the request
   * @param friendid The id of the user they want to add as a friend
   * @return A response entity with the status of the call
   * @throws Exception if there is an error communicating with the database
   */
  @PostMapping("/friends/add/{id}/{friendid}")
  public ResponseEntity<String> addFriend(@PathVariable("id") Integer id, @PathVariable("friendid") Integer friendid) throws Exception{
    Friend newFriend = new Friend(new User(id), new User(friendid), false);
    Friend found = friendRepository.findOneByUser1_IdAndUser2_Id(id,friendid);
    if(found != null){
      return new ResponseEntity<String> ("already exists!", HttpStatus.BAD_REQUEST);
      //return "This request already exists!";
    }
    found = friendRepository.findOneByUser1_IdAndUser2_Id(friendid,id);
    if(found != null){
      found.setConfirmed(true);
      newFriend.setConfirmed(true);
      friendRepository.save(found);
    }
    else {
      User user = userRepository.findById(id).get();
      Notification notification = new Notification(Notification.Type.FRIENDREQUEST, user);
      template.convertAndSend("/topic/notification/" + friendid, notification);
    }
    
    friendRepository.save(newFriend);
    return new ResponseEntity<String> ("success", HttpStatus.OK);
  }

  /**
   * @param id The id of the user sending the request
   * @param friendid The id of the user they no longer want to be friends with or want to decline their request
   * @return A response entity containing the status of the request
   * @throws Exception if there is an error communicating with the database
   */
  @PostMapping("/friends/delete/{id}/{friendid}")
  public ResponseEntity<String> removeFriend(@PathVariable("id") Integer id, @PathVariable("friendid") Integer friendid) throws Exception{
    Friend found = friendRepository.findOneByUser1_IdAndUser2_Id(id,friendid);
    if(found != null && found.isConfirmed()){
      friendRepository.delete(found);
      friendRepository.delete(friendRepository.findOneByUser1_IdAndUser2_Id(friendid,id));
    }
    else if(found != null){
      friendRepository.delete(found);
    }
    return new ResponseEntity<String> ("success", HttpStatus.OK);
  }

}