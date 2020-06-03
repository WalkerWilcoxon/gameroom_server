package bs7;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SubscriptionListener {

  @EventListener
  public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
    System.out.println(sessionSubscribeEvent.getMessage());
  }
}