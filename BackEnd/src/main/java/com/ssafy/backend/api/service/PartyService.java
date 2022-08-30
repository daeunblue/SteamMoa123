package com.ssafy.backend.api.service;

import com.ssafy.backend.api.request.PartyPostReq;
import com.ssafy.backend.api.request.PartyPutReq;
import com.ssafy.backend.api.response.EvaluatePartyDTO;
import com.ssafy.backend.api.response.PUserEvalDto;
import com.ssafy.backend.api.response.PartyCreateGamelistDTO;
import com.ssafy.backend.api.response.PartyDTO;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

public interface PartyService {

    // 파티 전체 목록
    JSONObject getPartyList(int page);

    // 파티 검색+필터+정렬 기반 목록
    /*
        필터링: 게임 이름, 파티 상태, 게임 장르
        정렬: 파티 생성 최근순 (디폴트), 마감 날짜 가까운 순, 게임 이름 순
     */
    JSONObject searchPartyList(int page, String searchString, String[] partyTags, String[] partyStatuses, String sortString);

    // 파티 생성
    String createParty(PartyPostReq PartyInfo);

    // 파티 생성시 게임ID 검색
    JSONObject searchPartyCreateGamelist(int page, String searchString);

    // 파티 상세 조회
    PartyDTO getPartyDetail(Long partyId);

    // 파티 수정
    String updateParty(Long partyId, PartyPutReq partyInfo);

    // 파티 삭제
    boolean deleteParty(Long partyId);

    // 파티 평가를 위한 정보 반환
    List<PUserEvalDto> getPlayersForEvaluate(Long partyId, String userServiceId);

    // 평가 페이지용 파티 상세 조회
    @Transactional
    EvaluatePartyDTO getPartyDetailForEvaluation(Long partyId, String userServiceId);

    // 파티원 참가
    String memberJoin(Long partyId, String userServiceId);

    // 파티원 탈퇴
    String memberLeave(Long partyId, String userServiceId);

    // 파티를 임의 모집마감하는 API
    boolean closeParty(Long partyId);

}
