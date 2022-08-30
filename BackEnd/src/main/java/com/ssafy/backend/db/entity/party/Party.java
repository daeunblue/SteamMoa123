package com.ssafy.backend.db.entity.party;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ssafy.backend.db.entity.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="party")
public class Party {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long partyId;

    // 단방향 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="game_id")
    private Game game;

    @Column(name = "party_title")
    private String title;

    @Column(name = "max_player")
    private int maxPlayer;

    @Column(name = "cur_player")
    private int curPlayer;

    @Column(name = "party_description", length = 5000)
    private String description;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "write_time")
    private LocalDateTime writeTime;

    @Column(name = "chat_link", length = 1024)
    private String chatLink;

    @Column(name = "is_closed", columnDefinition = "TINYINT", length = 1)
    private boolean isClosed;

    @Column(name = "party_status")
    private String status;

    // 양방향 일대다
    @OneToMany(mappedBy = "party", cascade = CascadeType.DETACH)
    @JsonManagedReference
    @NotFound(action = NotFoundAction.IGNORE)
    private List<PartyTag> partyTags = new ArrayList<>();

    // 양방향 일대다
    @OneToMany(mappedBy = "party", cascade = CascadeType.DETACH)
    @JsonManagedReference
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Puser> pusers = new ArrayList<>();

    // 양방향 편의 메소드 정의
    // 일대다
    public void addPartyTag(PartyTag partyTag) {
        this.partyTags.add(partyTag);
        if(partyTag.getParty() != this) {
            partyTag.setParty(this);
        }
    }

    // 일대다
    public void addPuser(Puser puser) {
        this.pusers.add(puser);
        if(puser.getParty() != this) {
            puser.setParty(this);
        }
    }
}
