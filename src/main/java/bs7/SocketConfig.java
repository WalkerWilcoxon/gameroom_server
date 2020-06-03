package bs7;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements
WebSocketMessageBrokerConfigurer {
  
  
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
      config.enableSimpleBroker("/topic", "/queue" ,"/user");
      config.setApplicationDestinationPrefixes("/app");
      config.setUserDestinationPrefix("/user");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
      registry.addEndpoint("/ws").setAllowedOrigins("*");
//      .withSockJS();
      //.setInterceptors(new SessionInterceptor());
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.setInterceptors(new UserInterceptor());
    
  }

  @Override
  public void configureClientOutboundChannel(ChannelRegistration registration) {
    
  }
}
