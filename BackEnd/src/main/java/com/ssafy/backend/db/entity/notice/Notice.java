package com.ssafy.backend.db.entity.notice;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 알림 모델 정의.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Notice {
    // 알림 식별자 PK
    @Id
    @Column(name = "notice_id")
    @GeneratedValue
    private Long noticeId;

    @Column(name = "user_service_id", nullable = false)
    private String userServiceId;

    @Column(name = "party_id", nullable = false)
    private Long partyId;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "is_read")
    @ColumnDefault("false")
    private boolean isRead;
}
