package com.ssafy.backend.db.entity.game;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssafy.backend.db.entity.review.Review;
import com.ssafy.backend.db.entity.tactic.Tactic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="game", indexes = @Index(name = "gamename_idx", columnList = "game_name"))
public class Game {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="game_id")
    private Long gameId;

    @Column(name="steam_game_id", length = 10, nullable = false)
    private String steamgameId;

    @Column(name="game_name", length = 512, nullable = false)
    private String name;

    @Column(name="game_score")
    private double score;

    @Column(name="game_img", length = 1024, nullable = false)
    private String imgpath;

    @Column(name="required_age")
    private int age;

    @Column(name="is_free", columnDefinition = "TINYINT", length = 1)
    private boolean isFree;

    @Column(name="game_description", length = 10000)
    private String description;

    @Column(name="supported_languages", length = 1024)
    private String languages;

    @Column(name="developers", length = 1024)
    private String developers;

    @Column(name="game_price")
    private int price;

    @Column(name="discount_per")
    private int discount;

    @Column(name="is_window", columnDefinition = "TINYINT", length = 1)
    private boolean isWindow;

    @Column(name="is_mac", columnDefinition = "TINYINT", length = 1)
    private boolean isMac;

    // 양방향 일대다 관계
    @OneToMany(mappedBy = "game")
    @JsonManagedReference
    private List<Gamegenre> gamegenres = new ArrayList<>();

    // 양방향 일대다 관계
    @OneToMany(mappedBy = "game")
    @JsonManagedReference
    private List<Gamecategory> gamecategories = new ArrayList<>();

    // 양방향 일대다 관계
    @OneToMany(mappedBy = "game")
    @JsonManagedReference
    private List<Tactic> tactics = new ArrayList<>();

    @OneToMany(mappedBy = "game")
    @JsonManagedReference
    private List<Review> reviewList = new ArrayList<>();

    // Constructor for unit test
    public Game(Long id, String sgi, String name){
        this.gameId = id;
        this.steamgameId = sgi;
        this.name = name;
    }


    public void addReview(Review review) {
        this.reviewList.add(review);
        if(review.getGame() != this){
            review.setGame(this);
        }
    }
}
