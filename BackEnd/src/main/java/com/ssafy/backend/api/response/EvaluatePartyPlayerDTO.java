package com.ssafy.backend.api.response;

import com.ssafy.backend.db.entity.party.Puser;
import lombok.Getter;
import lombok.Setter;

/*
    응답용 DTO.
    파티 상세 페이지에서 플레이어의 정보를 나타낼 때 사용.
 */
@Getter
@Setter
public class EvaluatePartyPlayerDTO {

    private Long playerId;
    private String playerName;
    private String userId;
    private boolean isLeader;
    private boolean isVoted;
    private boolean evalCompleted;

    public EvaluatePartyPlayerDTO(Puser pu) {
        this.playerId = pu.getUser().getUserId();
        this.playerName = pu.getUser().getUserName();
        this.userId = pu.getUser().getUserServiceId();
        this.isLeader = pu.isLeader();
    }
}
