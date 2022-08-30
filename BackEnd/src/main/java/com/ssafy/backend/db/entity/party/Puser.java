package com.ssafy.backend.db.entity.party;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ssafy.backend.db.entity.user.User;
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
@Table(name="puser")
public class Puser {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="puser_id")
    private Long puserId;

    // 양방향 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name="user_id")
    private User user;

    // 양방향 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name="party_id")
    private Party party;

    @Column(name="is_leader", columnDefinition = "TINYINT", length = 1)
    private boolean isLeader;

    // 양방향 편의 메소드 정의
    // 다대일
    public void setUser(User user) {
        if(this.user != null){
            this.user.getPusers().remove(this);
        }
        this.user = user;
        user.getPusers().add(this);
    }
}
