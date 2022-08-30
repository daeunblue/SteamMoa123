package com.ssafy.backend.db.repository.party;

import com.ssafy.backend.db.entity.game.Game;
import com.ssafy.backend.db.entity.party.Party;
import com.ssafy.backend.db.entity.party.Puser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long>, PartyCustomRepository {
    Optional<List<Party>> findAllByOrderByWriteTimeDesc();
    Optional<Party> findByPartyId(Long partyId);
    Optional<Party> findByPusersContains(Puser puser);
    Optional<List<Party>> findTop8ByGameOrderByWriteTimeDesc(Game game);
}
