package com.ssafy.backend.api.service;

import com.ssafy.backend.api.response.GameDTO;
import com.ssafy.backend.db.repository.game.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GameServiceImplTest {
    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameGenreRepository gameGenreRepository;
    @Mock
    private GGenreStorageRepository gGenreStorageRepository;
    @Mock
    private GameCategoryRepository gameCategoryRepository;
    @Mock
    private GCategoryStorageRepository gCategoryStorageRepository;

    @InjectMocks
    private GameServiceImpl service;

    @Test
    void getGameList() {
//        Game g = new Game(Long.parseLong("1"), "1234", "mygame");
//        gameRepository.save(g);
//        System.out.println(g.getGameId());
//        Gcategorystorage gcs = new Gcategorystorage(Long.parseLong("7"), "luckygame");
//        gCategoryStorageRepository.save(gcs);
//        Gamecategory gc = new Gamecategory(Long.parseLong("1"), g, gcs);
//        gameCategoryRepository.save(gc);
//        System.out.println(gameRepository.findByGameId(Long.parseLong("1")));
//        List<Game> list = service.getGameList();
//        System.out.println(list.size());
    }

    @Test
    void searchGameList() {
//        Game g = new Game(Long.parseLong("1"), "1234", "mygame");
//        gameRepository.save(g);
//        Gcategorystorage gcs = new Gcategorystorage(Long.parseLong("7"), "luckygame");
//        gCategoryStorageRepository.save(gcs);
//        Gamecategory gc = new Gamecategory(Long.parseLong("1"), g, gcs);
//        gameCategoryRepository.save(gc);

//        List<Game> list = service.searchGameList("star");
//        System.out.println(list);
    }

    @Test
    void getGameDetail() {
//        Game g = new Game(Long.parseLong("1"), "1234", "mygame");
//        gameRepository.save(g);
//        Gcategorystorage gcs = new Gcategorystorage(Long.parseLong("7"), "luckygame");
//        gCategoryStorageRepository.save(gcs);
//        Gamecategory gc = new Gamecategory(Long.parseLong("1"), g, gcs);
//        gameCategoryRepository.save(gc);

        GameDTO gdto = service.getGameDetail(Long.parseLong("1"));
        System.out.println(gdto);
    }
}