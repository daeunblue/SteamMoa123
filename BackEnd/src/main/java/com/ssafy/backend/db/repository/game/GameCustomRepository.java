package com.ssafy.backend.db.repository.game;

import com.ssafy.backend.db.entity.game.Game;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GameCustomRepository {
    Optional<List<Game>> findAllMultiGameByFilter(String name, String[] tag, Pageable pageable);
    Optional<List<Game>> findAllMultiGameByOnlyName(String name);
    int findAllMultiGameByFilter(String name, String[] tag);

    Optional<List<Game>> findAllMultiGameForBests();
    Optional<List<Game>> findAllMultiGameForFrees();
    Optional<List<Game>> findAllMultiGameForToday();
    Optional<List<Game>> findAllMultiGameForHots();
    Optional<List<Game>> findAllMultiGameForPicks();
    Optional<List<Game>> findTop15MultiGameForRandom();
}
