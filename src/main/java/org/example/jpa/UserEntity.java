package org.example.jpa;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.keyboard.FinalStateAutomate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "TG_entity")
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  private Long chatId;
  private Boolean subscribe;
  private Boolean admin;
  private FinalStateAutomate state = FinalStateAutomate.STORY;
  private Timestamp timer = Timestamp.from(Instant.now());
  private Long theMostLongTime = 0L;
  private FinalStateAutomate stateTimer;

  public void setState(FinalStateAutomate state) {
    if (!this.state.equals(state)) {
      if (Duration.between(timer.toInstant(),
              Instant.now()).compareTo(Duration.ofSeconds(theMostLongTime)) > 0) {
        stateTimer = this.state;
        theMostLongTime = Duration.between(timer.toInstant(), Instant.now()).toSeconds();
      }
      this.state = state;
      timer = Timestamp.from(Instant.now());
    }
  }
//    public FinalStateAutomate getState(){
//        return state;
//    }
}