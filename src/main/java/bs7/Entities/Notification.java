package bs7.Entities;

public class Notification {
  private Object data;
  private Type type;

  public Notification(Type type, Object data){
    this.data = data;
    this.type = type;
  }
  public enum Type {
    FRIENDUPDATE,
    FRIENDREQUEST,
    GAMEFOUND,
    MESSAGERECEIVED,
    GAMEINVITE
  }
  public Object getData() {
    return data;
  }
  public void setData(String data) {
    this.data = data;
  }
  public Type getType() {
    return type;
  }
  public void setType(Type type) {
    this.type = type;
  }
  
}
