package com.ssafy.backend.api.response;

import com.ssafy.backend.api.service.ReviewService;
import com.ssafy.backend.db.entity.game.Game;
import com.ssafy.backend.db.entity.game.Gamecategory;
import com.ssafy.backend.db.entity.game.Gamegenre;
import com.ssafy.backend.db.repository.review.ReviewRepository;
import com.ssafy.backend.db.repository.game.GCategoryStorageRepository;
import com.ssafy.backend.db.repository.game.GGenreStorageRepository;
import com.ssafy.backend.db.repository.game.GameCategoryRepository;
import com.ssafy.backend.db.repository.game.GameGenreRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/*
    Game + category들, genre들을 함께 반환해주도록
    서비스&컨트롤러 return 용으로 특별히 정의한 DTO입니다.

    [Spring Data JPA Tutorial] 13. Response 전용 DTO 클래스 이용하기
    출처: https://blog.jiniworld.me/155 [hello jiniworld:티스토리]
 */

@Getter @Setter
public class GameDTO {
    @Autowired
    private GameGenreRepository gameGenreRepository;
    @Autowired
    private GGenreStorageRepository gGenreStorageRepository;
    @Autowired
    private GameCategoryRepository gameCategoryRepository;
    @Autowired
    private GCategoryStorageRepository gCategoryStorageRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewService reviewService;

    private Long gameId;
    private String steamgameId;
    private String name;
    private int reviewScore = -1;
    private double score;
    private String imgpath;
    private int age;
    private boolean isFree;
    private String description;
    private String languages;
    private String developers;
    private int price;
    private int discount;
    private boolean isWindow;
    private boolean isMac;
    // 추가
    private List<String> genres;
    private List<String> categories;

    public GameDTO() {
    }

    public GameDTO(Game g, int reviewScore) {
        this.gameId = g.getGameId();
        this.steamgameId = g.getSteamgameId();
        this.name = g.getName();
        this.score = g.getScore();
        this.imgpath = g.getImgpath();
        this.age = g.getAge();
        this.isFree = g.isFree();
        this.description = g.getDescription();
        this.languages = g.getLanguages();
        this.developers = g.getDevelopers();
        this.price = g.getPrice();
        this.discount = g.getDiscount();
        this.isWindow = g.isWindow();
        this.isMac = g.isMac();
        this.reviewScore = reviewScore;

        List<String> genres_temp = new ArrayList<>();
        for (Gamegenre gameGenre : g.getGamegenres())
            genres_temp.add(gameGenre.getGenre().getGenre());
        this.genres = genres_temp;

        List<String> categories_temp = new ArrayList<>();
        for (Gamecategory gameCategory : g.getGamecategories())
            categories_temp.add(gameCategory.getCategory().getCategory());
        this.categories = categories_temp;
    }

    public GameDTO(Game g) {
        this.gameId = g.getGameId();
        this.steamgameId = g.getSteamgameId();
        this.name = g.getName();
        this.score = g.getScore();
        this.imgpath = g.getImgpath();
        this.age = g.getAge();
        this.isFree = g.isFree();
        this.description = g.getDescription();
        this.languages = g.getLanguages();
        this.developers = g.getDevelopers();
        this.price = g.getPrice();
        this.discount = g.getDiscount();
        this.isWindow = g.isWindow();
        this.isMac = g.isMac();

        List<String> genres_temp = new ArrayList<>();
        for (Gamegenre gameGenre : g.getGamegenres())
            genres_temp.add(gameGenre.getGenre().getGenre());
        this.genres = genres_temp;

        List<String> categories_temp = new ArrayList<>();
        for (Gamecategory gameCategory : g.getGamecategories())
            categories_temp.add(gameCategory.getCategory().getCategory());
        this.categories = categories_temp;
    }
}
