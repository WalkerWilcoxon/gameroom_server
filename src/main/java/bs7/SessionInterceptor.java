package bs7;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class SessionInterceptor implements HandshakeInterceptor {

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
          Map attributes) throws Exception {
      if (request instanceof ServletServerHttpRequest) {
          ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
          HttpSession session = servletRequest.getServletRequest().getSession();
          attributes.put("id", servletRequest.getHeaders().get("id"));
          attributes.put("sessionId", session.getId());
      }
      return true;
  }

  @Override
  public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
    // TODO Auto-generated method stub
    
  }
}