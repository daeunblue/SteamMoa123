package com.ssafy.backend.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
    응답용 DTO.
    알림 발송 전용.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
    private Long noticeId;
    private String userServiceId;
    private Long partyId;
    private String partyName;
    private String gameName;
    private String noticeTime;
    private boolean noticeIsRead;
}
