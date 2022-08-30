package com.ssafy.backend.api.service;

import com.ssafy.backend.db.entity.game.Game;
import com.ssafy.backend.api.response.GameDTO;
import com.ssafy.backend.api.response.GamelistDTO;
import com.ssafy.backend.db.entity.review.Review;
import com.ssafy.backend.db.repository.review.ReviewRepository;
import com.ssafy.backend.db.repository.game.GameRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service("gameService")
public class GameServiceImpl implements GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public JSONObject getGameList(int page) {
        Pageable pageable = PageRequest.of(page, 12);
        List<GamelistDTO> resultlist = new ArrayList<>();
        List<Game> games = gameRepository.findAllMultiGame(pageable).orElse(Collections.EMPTY_LIST);
        for (Game g: games) {
            List<Review> reviews = reviewRepository.findAllByGameGameId(g.getGameId()).get();
            System.out.println("리뷰 개수: "+reviews.size());
            if(reviews.size() == 0)
                resultlist.add(new GamelistDTO(g));
            else {
                int review_evg = 0;
                for (Review r: reviews) {
                    System.out.println("리뷰 점수: "+r.getReviewScore());
                    review_evg += r.getReviewScore();
                }
                review_evg = Math.round((float) review_evg / reviews.size());
                resultlist.add(new GamelistDTO(g, review_evg));
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxPage", Integer.toString((gameRepository.findAllMultiGame()-1)/12+1));

        JSONArray data = new JSONArray();
        for (GamelistDTO g : resultlist) {
            data.add(g);
        }

        jsonObject.put("data", data);

        return jsonObject;
    }

    @Override
    public JSONObject searchGameList(int page, String searchString, String[] tags) {
        Pageable pageable = PageRequest.of(page, 12);
        List<GamelistDTO> resultlist = new ArrayList<>();

        List<Game> games = gameRepository.findAllMultiGameByFilter(searchString, tags, pageable).orElse(Collections.EMPTY_LIST);
        for (Game g: games) {
            List<Review> reviews = reviewRepository.findAllByGameGameId(g.getGameId()).get();
            System.out.println("리뷰 개수: "+reviews.size());
            if(reviews.size() == 0)
                resultlist.add(new GamelistDTO(g));
            else {
                int review_evg = 0;
                for (Review r: reviews) {
                    System.out.println("리뷰 점수: "+r.getReviewScore());
                    review_evg += r.getReviewScore();
                }
                review_evg = Math.round((float) review_evg / reviews.size());
                resultlist.add(new GamelistDTO(g, review_evg));
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxPage", Integer.toString((gameRepository.findAllMultiGameByFilter(searchString, tags)-1)/12+1));

        JSONArray data = new JSONArray();
        for (GamelistDTO g : resultlist) {
            data.add(g);
        }

        jsonObject.put("data", data);

        return jsonObject;
    }

    @Override
    public GameDTO getGameDetail(Long gameId) {
        Game g = gameRepository.findByGameId(gameId).orElse(null);
        GameDTO gameDetail;

        if(g == null)
            return null;

        List<Review> reviews = reviewRepository.findAllByGameGameId(g.getGameId()).get();
        if(reviews.size() == 0)
            gameDetail = new GameDTO(g);
        else {
            int review_evg = 0;
            for (Review r: reviews) {
                System.out.println("리뷰 점수: "+r.getReviewScore());
                review_evg += r.getReviewScore();
            }
            review_evg = Math.round((float) review_evg / reviews.size());
            gameDetail = new GameDTO(g, review_evg);
        }

        return gameDetail;
    }
}
