package org.example.jpa;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "st")
public class StateEntity2 {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "story")
  private String story;

  @Column(name = "season")
  private String seasons;

  @Column(name = "episode")
  private String episode;
}
