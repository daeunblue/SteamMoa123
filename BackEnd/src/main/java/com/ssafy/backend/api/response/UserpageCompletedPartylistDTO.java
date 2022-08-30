package com.ssafy.backend.api.response;

import com.ssafy.backend.db.entity.party.Party;
import com.ssafy.backend.db.entity.party.PartyTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*
    응답용 DTO.
    마이페이지에서 완료된 파티를 리스트로 보여줄 때 사용.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserpageCompletedPartylistDTO extends PartylistDTO {

    private boolean voted;

    public UserpageCompletedPartylistDTO(Party p) {
        super(p);
    }

    public UserpageCompletedPartylistDTO(Party p, boolean voted) {

        super(p);
        this.voted = voted;
    }
}
