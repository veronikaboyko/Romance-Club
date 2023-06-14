package org.example.jpa;

import lombok.*;
import org.example.keyboard.FinalStateAutomate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "stats")
@TypeDef(name = "state_type", typeClass = StateType.class)
public class StatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Type(type = "state_type")
    @Column(name = "s_state", nullable = false)
    private FinalStateAutomate state;
}
