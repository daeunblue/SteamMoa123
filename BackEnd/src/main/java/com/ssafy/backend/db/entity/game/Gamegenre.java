package com.ssafy.backend.db.entity.game;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="gamegenre")
public class Gamegenre {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="game_genre_id")
    private Long gamegenreId;

    // 양방향 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name="game_id")
    private Game game;

    // 단방향 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="genre_id")
    private Ggenrestorage genre;
}
