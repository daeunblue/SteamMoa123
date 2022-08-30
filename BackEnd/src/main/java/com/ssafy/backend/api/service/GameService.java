package com.ssafy.backend.api.service;

import com.ssafy.backend.api.response.GameDTO;
import org.json.simple.JSONObject;

/**
 *	게임 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface GameService {
    // 게임 전체 목록 조회: 상세정보 필요 없음
    JSONObject getGameList(int page);

    // 게임 검색
    JSONObject searchGameList(int page, String searchString, String[] tags);

    // 게임 상세 정보 조회
    GameDTO getGameDetail(Long gameId);
}
