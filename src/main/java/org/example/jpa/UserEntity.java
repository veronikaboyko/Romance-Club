package org.example.jpa;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;
import org.example.keyboard.FinalStateAutomate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * класс пользователя для базы данных.
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "users")
@TypeDef(name = "state_type", typeClass = StateType.class)
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "chatid")
  private Long chatId;
  @Column(name = "subscribe")
  private Boolean subscribe;
  @Column(name = "admin")
  private Boolean admin;
  @Enumerated(EnumType.STRING)
  @Type(type = "state_type")
  @Column(name = "cur_state")
  private FinalStateAutomate state = FinalStateAutomate.START;
  @Column(name = "timer")
  private Timestamp timer = Timestamp.from(Instant.now());
  @Column(name = "themostlongtimer")
  private Long theMostLongTime = 0L;
  @Enumerated(EnumType.STRING)
  @Type(type = "state_type")
  @Column(name = "statetimer")
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
}