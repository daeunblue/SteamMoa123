package com.ssafy.backend.db.repository.game;

import com.ssafy.backend.db.entity.game.Gcategorystorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GCategoryStorageRepository extends JpaRepository<Gcategorystorage, Long> {
    Optional<Gcategorystorage> findByCategoryId(Long categoryId);

    Optional<Gcategorystorage> findByCategory(String category);

}
