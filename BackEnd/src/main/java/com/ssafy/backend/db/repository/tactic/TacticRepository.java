package com.ssafy.backend.db.repository.tactic;

import com.ssafy.backend.db.entity.tactic.Tactic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TacticRepository extends JpaRepository<Tactic, Long> {
    Optional<List<Tactic>> findByUserUserId(Long userId); // user id 기반으로 공략 리스트 반환
    Optional<List<Tactic>> findByGameGameId(Long gameId); // game id 기반으로 공략 리스트 반환
    Optional<List<Tactic>> findTop8ByGameGameIdOrderByCreateTimeDesc(Long gameId); // game id 기반으로 공략 리스트 반환
    Optional<Tactic> findByTacticId(Long tacticId);
}
