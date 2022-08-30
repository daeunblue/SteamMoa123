package com.ssafy.backend.api.response;

import com.ssafy.backend.db.entity.game.Game;
import com.ssafy.backend.db.entity.game.Gamegenre;
import com.ssafy.backend.db.repository.review.ReviewRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/*
    게임 리스트 조회, 게임 리스트 검색 API를 통해 게임 리스트를 보낼 때 사용할 DTO이다.
 */
@Getter
@Setter
@NoArgsConstructor
public class GamelistDTO {
    @Autowired
    private ReviewRepository reviewRepository;

    private Long gameId;
    private String gameName;
    private List<String> gameTags;
    private String gameImgpath;
    private int gameReviewScore = -1;
    private int gamePrice;

    public GamelistDTO(Game g, int reviewScore) {
        this.gameId = g.getGameId();
        this.gameName = g.getName();
        this.gameTags = new ArrayList<>();
        this.gameImgpath = g.getImgpath();
        this.gamePrice = g.getPrice();
        this.gameReviewScore = reviewScore;

        for (Gamegenre gameGenre : g.getGamegenres())
            this.gameTags.add(gameGenre.getGenre().getGenre());
    }

    public GamelistDTO(Game g) {
        this.gameId = g.getGameId();
        this.gameName = g.getName();
        this.gameTags = new ArrayList<>();
        this.gameImgpath = g.getImgpath();
        this.gamePrice = g.getPrice();

        for (Gamegenre gameGenre : g.getGamegenres())
            this.gameTags.add(gameGenre.getGenre().getGenre());
    }
}
