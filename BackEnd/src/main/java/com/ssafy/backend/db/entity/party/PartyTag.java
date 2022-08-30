package com.ssafy.backend.db.entity.party;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="partytag")
public class PartyTag {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="partytag_id")
    private Long partytagId;

    // 양방향 다대일
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
    @JoinColumn(name="party_id")
    private Party party;

    // 단방향 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name="ptag_id")
    private PtagStorage ptagStorage;

}
