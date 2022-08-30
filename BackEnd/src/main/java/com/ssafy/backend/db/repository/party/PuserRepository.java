package com.ssafy.backend.db.repository.party;

import com.ssafy.backend.db.entity.user.User;
import com.ssafy.backend.db.entity.party.Party;
import com.ssafy.backend.db.entity.party.Puser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PuserRepository extends JpaRepository<Puser, Long> {
    Optional<List<Puser>> findAllByUserAndParty(User user, Party party);
    Optional<List<Puser>> findAllByUserOrderByPuserIdDesc(User user);
    Optional<List<Puser>> findTop3ByUserOrderByPuserIdDesc(User user);
}
