package bs7.Controllers;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import bs7.Entities.DMessage;
import bs7.Entities.Notification;
import bs7.Entities.User;
import bs7.Repository.MessageRepository;
import bs7.Repository.UserRepository;

@RestController
public class DMController {
  @Autowired
  private SimpMessagingTemplate template;
  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private UserRepository userRepository;

  @GetMapping("/directMessage/{id}/{id2}")
  public Iterable<DMessage> getDirectMessages(@PathVariable("id") Integer id, @PathVariable("id2") Integer id2){
    List<DMessage> list = messageRepository.findAllByFromAndTo(new User(id), new User(id2));
    list.addAll(messageRepository.findAllByFromAndTo(new User(id2),new User(id)));
    Collections.sort(list);
    return list;
  }
  @MessageMapping("directMessage/{id}/{id2}")
  public void directMessage(@DestinationVariable int id, @DestinationVariable int id2, String message){
    System.out.println(id + " said:  " + message + " to  " +id2);
    User from = userRepository.findById(id).get();
    User to = userRepository.findById(id2).get();
    DMessage dmessage = new DMessage(from, to, message);
    messageRepository.save(dmessage);
    Notification notification = new Notification(Notification.Type.MESSAGERECEIVED, dmessage);
    template.convertAndSend("/topic/notification/" + id, notification);
    template.convertAndSend("/topic/notification/" + id2, notification);
  }
}
