package com.ssafy.backend.db.repository.game;

import com.ssafy.backend.db.entity.game.Gamecategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameCategoryRepository extends JpaRepository<Gamecategory, Long> {
    Optional<List<Gamecategory>> findAllByGame_GameId(Long gameId);

    Optional<List<Gamecategory>> findAllByCategory_CategoryId(Long categoryId);

}
