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
    파티를 리스트로 보여줄 때 사용.
 */
@Getter
@Setter
@NoArgsConstructor
public class PartylistDTO {

    private Long partyId;

    private Long gameId;
    private String gameImgPath;
    private String gameName;

    private String partyTitle;
    private int maxPlayer;
    private int curPlayer;
    private String startTime;
    private String writeTime;
    private String partyStatus;
    private List<String> partyTags = new ArrayList<>();

    private boolean partyIsUrgent;

    public PartylistDTO(Party p){
        this.partyId = p.getPartyId();
        this.gameId = p.getGame().getGameId();
        this.gameImgPath = p.getGame().getImgpath();
        this.gameName = p.getGame().getName();
        this.partyTitle = p.getTitle();
        this.maxPlayer = p.getMaxPlayer();
        this.curPlayer = p.getCurPlayer();
        this.startTime = p.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        this.writeTime = p.getWriteTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        this.partyStatus = p.getStatus();
        for (PartyTag pt: p.getPartyTags()) {
            this.partyTags.add(pt.getPtagStorage().getContent());
        }
        if(this.partyStatus.equals("1") && p.getStartTime().isBefore(LocalDateTime.now().plusHours(9).plusDays(1)))
            this.partyIsUrgent = true;
        else
            this.partyIsUrgent = false;

        System.out.println("파티 list DTO 생성: "+this.partyTitle);
    }
}
