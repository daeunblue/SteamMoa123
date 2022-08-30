package com.ssafy.backend.api.response;

import com.ssafy.backend.db.entity.party.Party;
import com.ssafy.backend.db.entity.party.PartyTag;
import com.ssafy.backend.db.entity.party.Puser;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*
    응답용 DTO.
    파티를 상세 페이지로 보여줄 때 사용.
 */
@Getter
@Setter
public class EvaluatePartyDTO {

    private Long gameId;
    private String gameImgPath;
    private String gameName;

    private Long partyId;
    private String partyTitle;
    private List<String> partyTags = new ArrayList<>();
    private int maxPlayer;
    private int curPlayer;
    private String startTime;
    private String writeTime;
    private String writerId;
    private String partyStatus;
    private List<EvaluatePartyPlayerDTO> partyPlayers = new ArrayList<>();
    private String partyDescription;
    private String chatLink;
    private boolean partyIsUrgent;
    private boolean evalCompleted;

    public EvaluatePartyDTO(Party p){
        this.gameId = p.getGame().getGameId();
        this.gameImgPath = p.getGame().getImgpath();
        this.gameName = p.getGame().getName();
        this.partyId = p.getPartyId();
        this.partyTitle = p.getTitle();
        for (PartyTag partytag: p.getPartyTags()) {
            this.partyTags.add(partytag.getPtagStorage().getContent());
        }
        this.maxPlayer = p.getMaxPlayer();
        this.curPlayer = p.getCurPlayer();
        this.startTime = p.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        this.writeTime = p.getWriteTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        this.partyStatus = p.getStatus();
        for (Puser puser: p.getPusers()) {
            this.partyPlayers.add(new EvaluatePartyPlayerDTO(puser));
            if(puser.isLeader())
                this.writerId = puser.getUser().getUserServiceId();
        }
        this.partyDescription = p.getDescription();
        this.chatLink = p.getChatLink();
        if(this.partyStatus.equals("1") && p.getStartTime().isBefore(LocalDateTime.now().plusHours(9).plusDays(1)))
            this.partyIsUrgent = true;
        else
            this.partyIsUrgent = false;
    }
}
