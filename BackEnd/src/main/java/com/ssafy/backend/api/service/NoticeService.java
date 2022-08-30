package com.ssafy.backend.api.service;

import com.ssafy.backend.api.response.NoticeDTO;

import java.util.List;

/**
 *	게임 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface NoticeService {
    // 알림 생성
    String createNotice(String userServiceId, Long partyId);

    // 알림 목록 조회
    List<NoticeDTO> getNoticeList(String userServiceId);

    // 알림 삭제
    String deleteNotice(Long NoticeId);

    // 알림 수정 (읽음 확인)
    String readNotice(Long NoticeId);
}
