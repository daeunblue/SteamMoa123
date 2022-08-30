package com.ssafy.backend.db.repository.game;

import com.ssafy.backend.db.entity.game.Ggenrestorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GGenreStorageRepository extends JpaRepository<Ggenrestorage, Long> {
    Optional<Ggenrestorage> findByGenreId(Long genreId);
    Optional<Ggenrestorage> findByGenre(String genre);
}
