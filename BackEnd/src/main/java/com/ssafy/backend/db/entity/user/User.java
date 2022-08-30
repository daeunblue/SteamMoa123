package com.ssafy.backend.db.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.ssafy.backend.db.entity.review.Review;
import com.ssafy.backend.db.entity.party.Puser;
import com.ssafy.backend.db.entity.tactic.Tactic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 유저 모델 정의.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class User {
    // 유저 식별자 PK
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_steam_id", nullable = false)
    private String userSteamId;

    @Column(name = "user_service_id", nullable = false, unique = true)
    private String userServiceId;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // 왜 쓰는지 -> https://eglowc.tistory.com/28
    @Column(name = "user_service_pw")
    private String password;

    @Column(name = "user_name", nullable = false)
    private String userName;

    // Enum 타입으로 바꿀지 고민중임.
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Column(name = "user_point", nullable = false)
    private Double userPoint;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    @NotFound(action = NotFoundAction.IGNORE)
    private List<UserTag> uTagLists = new ArrayList<>();

    // 양방향 일대다
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Puser> pusers = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Tactic> tacticList = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Review> reviewList = new ArrayList<>();

    @Column(name = "is_deleted")
    @ColumnDefault("0")
    private Boolean isDeleted;

    @Override
    public String toString() {
        return this.userId +", "+this.userServiceId+", "+this.userPoint;
    }
    // mappedBy : (주인이 아닌 테이블엔티티에 붙임 + 기준 : 주인 테이블에서의 객체명)

    // 양방향 편의 메소드 정의
    // 일대다
    public void addReview (Review review){
        this.reviewList.add(review);
        if(review.getUser() != this){
            review.setUser(this);
        }
    }
    // 일대다
    public void addUTagLists(UserTag userTag) {
        this.uTagLists.add(userTag);
        if(userTag.getUser() != this) {
            userTag.setUser(this);
        }
    }
}
