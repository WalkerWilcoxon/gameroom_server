package bs7.Entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
@Entity(name= "dmessages")
public class DMessage implements Comparable{

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;
  @ManyToOne
  @JoinColumn(name ="sender")
  private User from;
  @ManyToOne
  @JoinColumn(name ="receiver")
  private User to;
  private String message;
  @CreationTimestamp
  private LocalDateTime time;
  
  public DMessage(){}
  public DMessage(User from, User to, String message) {
    super();
    this.from = from;
    this.to = to;
    this.message = message;
  }
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public User getFrom() {
    return from;
  }
  public void setFrom(User from) {
    this.from = from;
  }
  public User getTo() {
    return to;
  }
  public void setTo(User to) {
    this.to = to;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public LocalDateTime getTime() {
    return time;
  }
  public void setTime(LocalDateTime time) {
    this.time = time;
  }
  @Override
  public int compareTo(Object arg0) {
    return this.time.compareTo(((DMessage)arg0).time);
  }
  
}
