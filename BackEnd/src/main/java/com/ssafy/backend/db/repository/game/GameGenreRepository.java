package com.ssafy.backend.db.repository.game;

import com.ssafy.backend.db.entity.game.Gamegenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameGenreRepository extends JpaRepository<Gamegenre, Long> {
    Optional<List<Gamegenre>> findAllByGame_GameId(Long gameId);

    Optional<List<Gamegenre>> findAllByGenre_GenreId(Long genreId);

}
