package com.ssafy.backend.db.repository.party;

import com.ssafy.backend.db.entity.party.Pvote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PvoteRepository extends JpaRepository<Pvote, Long> {
    Optional<List<Pvote>> findAllByPartyId(Long partyId);
}
