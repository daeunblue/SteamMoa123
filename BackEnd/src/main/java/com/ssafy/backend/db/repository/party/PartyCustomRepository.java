package com.ssafy.backend.db.repository.party;

import com.ssafy.backend.db.entity.party.Party;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PartyCustomRepository {

    Optional<List<Party>> findAllPartyByFilter(String searchString, String[] partyTags, String[] partyStatuses, String sortString, Pageable pageable);
    int findAllPartyByFilter(String searchString, String[] partyTags, String[] partyStatuses, String sortString);

}
