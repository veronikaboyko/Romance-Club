package org.example.JPA;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.keyboard.FinalStateAutomate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

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
    private FinalStateAutomate state;
    private Timestamp timer;

    @PrePersist
    public void perSiest(){
        timer = Timestamp.from(Instant.now());
    }
}