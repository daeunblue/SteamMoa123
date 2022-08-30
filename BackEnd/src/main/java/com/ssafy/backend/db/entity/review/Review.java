package com.ssafy.backend.db.entity.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ssafy.backend.db.entity.user.User;
import com.ssafy.backend.db.entity.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="review")
public class Review {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="review_id")
    private Long reviewId;

    @Column(name="review_score", nullable = false)
    private Double reviewScore;

    @Column(name="review_content", nullable = false)
    private String reviewContent;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="game_id")
    private Game game;

    @Column(name="local_date_time", nullable = false)
    private LocalDateTime localDateTime;

    public void setUser(User user) {
        if(this.user != null) {
            this.user.getReviewList().remove(this);
        }
        this.user = user;
        if(!user.getReviewList().contains(this)) {
            user.addReview(this);
        }
    }

    public void setGame(Game game) {
        if(this.game != null) {
            this.game.getReviewList().remove(this);
        }
        this.game = game;
        System.out.println(game.getReviewList());
        if(!game.getReviewList().contains(this)) {
            game.addReview(this);
        }
    }

}
