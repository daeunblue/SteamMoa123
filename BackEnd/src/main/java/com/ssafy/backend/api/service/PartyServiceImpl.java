package com.ssafy.backend.api.service;

import com.ssafy.backend.api.request.PartyPostReq;
import com.ssafy.backend.api.request.PartyPutReq;
import com.ssafy.backend.api.response.*;
import com.ssafy.backend.db.entity.game.Game;
import com.ssafy.backend.db.entity.user.User;
import com.ssafy.backend.db.entity.party.*;
import com.ssafy.backend.db.repository.party.*;
import com.ssafy.backend.db.repository.user.UserRepository;
import com.ssafy.backend.db.repository.game.GameRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("partyService")
public class PartyServiceImpl implements PartyService{
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PtagStorageRepository ptagStorageRepository;
    @Autowired
    private PartyTagRepository partyTagRepository;
    @Autowired
    private PuserRepository puserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PvoteRepository pvoteRepository;

    // 파티 전체 목록
    @Override
    @Transactional
    public JSONObject getPartyList(int page) {
        Pageable pageable = PageRequest.of(page, 12);
        List<PartylistDTO> resultlist = new ArrayList<>();
        System.out.println("쿼리수행결과 개수: "+partyRepository.findAllByOrderByWriteTimeDesc().orElse(null).size());
        partyRepository.findAll().forEach(Party->resultlist.add(new PartylistDTO(Party)));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxPage", Integer.toString((partyRepository.findAll().size()-1)/12+1));

        System.out.println("resultlist 개수: "+resultlist.size());
        System.out.println("resultlist: "+resultlist.get(0).getPartyTitle());

        JSONArray data = new JSONArray();
        for (PartylistDTO p : resultlist) {
            data.add(p);
        }

        jsonObject.put("data", data);

        return jsonObject;

    }

    // 파티 검색+필터+정렬 기반 목록
    /*
        필터링: 게임 이름, 파티 상태, 게임 장르
        정렬: 파티 생성 최근순 (디폴트), 마감 날짜 가까운 순, 게임 이름 순
     */
    @Override
    @Transactional
    public JSONObject searchPartyList(int page, String searchString, String[] partyTags, String[] partyStatuses, String sortString) {
        Pageable pageable = PageRequest.of(page, 12);
        List<PartylistDTO> resultlist = new ArrayList<>();
        partyRepository.findAllPartyByFilter(searchString, partyTags, partyStatuses, sortString, pageable).orElse(Collections.EMPTY_LIST).forEach(Party->resultlist.add(new PartylistDTO((Party)Party)));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxPage", Integer.toString((partyRepository.findAllPartyByFilter(searchString, partyTags, partyStatuses, sortString)-1)/12+1));

        JSONArray data = new JSONArray();
        for (PartylistDTO p : resultlist) {
            data.add(p);
        }

        jsonObject.put("data", data);

        return jsonObject;
    }

    // 파티 생성
    /*
        파티 코드
        모집 중: 1
        모집 완료: 2
        플레이 중: 3
        플레이 완료: 4
        모집 실패: 5
     */
    @Override
    @Transactional
    public String createParty(PartyPostReq partyInfo) {
        Party party = new Party();

        // 멀티게임 맞는지 확인 후 실패 응답
        if (!gameRepository.findAllMultiGameByOnlyName("").orElse(Collections.EMPTY_LIST).contains(gameRepository.findByGameId(partyInfo.getGameId()).orElse(null)))
            return "fail: 유효한 game id가 아닙니다.";
        // 파티 태그 존재하는지 확인 후 실패 응답
        for (String tag:partyInfo.getPartyTags()) {
            if (!ptagStorageRepository.findById(Long.parseLong(tag)).isPresent())
                return "fail: 유효한 파티 태그 코드가 아닙니다.";
        }
        // 유저 존재하는지 확인 후 실패 응답
        if (!userRepository.findByUserServiceId(partyInfo.getUserId()).isPresent())
            return "fail: 유효한 유저 아이디가 아닙니다.";
        // 최대인원은 2명 이상
        if (Integer.parseInt(partyInfo.getMaxPlayer()) < 2)
            return "fail: 파티 최대인원은 2명 이상이어야 합니다.";

        party.setGame(gameRepository.findByGameId(partyInfo.getGameId()).orElse(null));
        party.setTitle(partyInfo.getPartyTitle());
        party.setMaxPlayer(Integer.parseInt(partyInfo.getMaxPlayer()));
        party.setCurPlayer(1);
        party.setDescription(partyInfo.getPartyDescription());
        party.setStartTime(LocalDateTime.parse(partyInfo.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        party.setWriteTime(LocalDateTime.now().plusHours(9));
        party.setChatLink(partyInfo.getChatLink());
        party.setStatus("1");


        PartyTag partyTag;
        for (String tag:partyInfo.getPartyTags()) {
            partyTag = new PartyTag();
            partyTag.setPtagStorage(ptagStorageRepository.findById(Long.parseLong(tag)).get());
            partyTagRepository.save(partyTag);
            party.addPartyTag(partyTag);
        }

        // 유저 존재하는지 확인 후 실패 응답
        Optional<User> user = userRepository.findByUserServiceId(partyInfo.getUserId());
        Puser puser = new Puser();
        puser.setUser(user.get());
        puser.setLeader(true);
        puserRepository.save(puser);
        party.addPuser(puser);


        partyRepository.save(party);

        return "success";
    }

    // 파티 생성시 게임ID 검색
    @Override
    @Transactional
    public JSONObject searchPartyCreateGamelist(int page, String searchString) {
        Pageable pageable = PageRequest.of(page, 10);
        List<PartyCreateGamelistDTO> resultlist = new ArrayList<>();

        List<Game> games = gameRepository.findAllMultiGameByFilter(searchString, new String[]{} , pageable).orElse(Collections.EMPTY_LIST);
        for (Game g: games) {
                resultlist.add(new PartyCreateGamelistDTO(g));
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxPage", Integer.toString((gameRepository.findAllMultiGameByFilter(searchString, new String[]{})-1)/10+1));

        JSONArray data = new JSONArray();
        for (PartyCreateGamelistDTO g : resultlist) {
            data.add(g);
        }

        jsonObject.put("data", data);

        return jsonObject;
    }


    // 파티 상세 조회
    @Override
    @Transactional
    public PartyDTO getPartyDetail(Long partyId) {
        Party party = partyRepository.findByPartyId(partyId).orElse(null);

        if(party == null)
            return null;

        else
            return new PartyDTO(party);
    }

    // 파티 수정
    @Override
    @Transactional
    public String updateParty(Long partyId, PartyPutReq partyInfo) {
        Party party = partyRepository.findByPartyId(partyId).orElse(null);

        // 파티 설명 수정, 파티 디스코드 링크 수정
        party.setDescription(partyInfo.getPartyDescription());
        party.setChatLink(partyInfo.getChatLink());

        // 태그 수정
        for (PartyTag pt: party.getPartyTags()) {
            partyTagRepository.delete(pt);
        }
        party.setPartyTags(new ArrayList<>());

        PartyTag partyTag;
        for (String tag:partyInfo.getPartyTags()) {
            partyTag = new PartyTag();
            partyTag.setPtagStorage(ptagStorageRepository.findById(Long.parseLong(tag)).get());
            partyTagRepository.save(partyTag);
            party.addPartyTag(partyTag);
        }

        partyRepository.save(party);

        return "success";
    }

    // 파티 삭제
    @Override
    @Transactional
    public boolean deleteParty(Long partyId) {
        try{
            Party curParty = partyRepository.findByPartyId(partyId).orElse(null);
            if(curParty == null)
                return false;

            // 파티 태그 삭제
            for (PartyTag pt: curParty.getPartyTags()) {
                partyTagRepository.delete(pt);
            }
            // 파티 사용자 삭제
            for (Puser pu: curParty.getPusers()) {
                pu.getUser().getPusers().remove(pu);
                puserRepository.delete(pu);
            }

            partyRepository.delete(curParty);
            return true;
        }catch (Exception e){
            System.out.println("Party 삭제 요청 실패");
            return false;
        }
    }

    @Override
    public List<PUserEvalDto> getPlayersForEvaluate(Long partyId, String userServiceId) {
        // 파티원들의 정보 저장
        List<PUserEvalDto> list = new ArrayList<>();

        List<Puser> pUserList = partyRepository.findByPartyId(partyId).orElse(null).getPusers();

        for (Puser puser: pUserList) {
            PUserEvalDto pUserEvalDto = new PUserEvalDto();
            if(puser.getUser().getUserServiceId().equals(userServiceId)) continue; // 본인은 제외하고 평가 리스트 보내기
            pUserEvalDto.setUserServiceId(puser.getUser().getUserServiceId());
            pUserEvalDto.setUserId(puser.getPuserId());
            list.add(pUserEvalDto);
        }

//        try{
//
//        }catch (Exception e){
//
//        }

        return list;
    }

    // 평가 페이지용 파티 상세 조회
    @Override
    @Transactional
    public EvaluatePartyDTO getPartyDetailForEvaluation(Long partyId, String userServiceId) {
        EvaluatePartyDTO evaluatePartyDTO = new EvaluatePartyDTO(partyRepository.findByPartyId(partyId).orElse(null));

        Set<String> evaluatedMembers = new HashSet<>();
        Set<Long> votingMembers = new HashSet<>();
        for(Pvote pvote: (List<Pvote>) pvoteRepository.findAllByPartyId(partyId).orElse(Collections.EMPTY_LIST)){
            evaluatedMembers.add(pvote.getUserServiceId());
            votingMembers.add(pvote.getVoterId());
        }

        if(votingMembers.contains(userRepository.findByUserServiceId(userServiceId).get().getUserId()))
            evaluatePartyDTO.setEvalCompleted(true);

        for(EvaluatePartyPlayerDTO pp: evaluatePartyDTO.getPartyPlayers()){
            if(evaluatedMembers.contains(pp.getUserId())) {
                pp.setVoted(true);
            }
            if(votingMembers.contains(pp.getPlayerId())) {
                pp.setEvalCompleted(true);
            }
        }

        EvaluatePartyPlayerDTO epp_fordelete = null;
        for (EvaluatePartyPlayerDTO epp: evaluatePartyDTO.getPartyPlayers()) {
            if(epp.getUserId().equals(userServiceId))
                epp_fordelete = epp;
        }
        evaluatePartyDTO.getPartyPlayers().remove(epp_fordelete);

        return evaluatePartyDTO;
    }


    @Override
    public String memberJoin(Long partyId, String userServiceId) {
        Party party = partyRepository.findByPartyId(partyId).orElse(null);

        // 오류: 유효하지 않은 사용자 아이디입니다.
        if(!userRepository.findByUserServiceId(userServiceId).isPresent())
            return "fail: 유효하지 않은 사용자 아이디입니다.";
        // 오류: 이미 파티에 있습니다
        for (Puser puser: party.getPusers()) {
            if(puser.getUser().getUserServiceId().equals(userServiceId))
                return "fail: 이 유저가 이미 해당 파티에 참가하고 있습니다.";
        }
        // 오류: 파티원 초과
        if(party.getCurPlayer()+1 > party.getMaxPlayer())
            return "fail: 최대 파티 인원을 초과하였습니다.";
        // 오류: 파티 상태
        if(!party.getStatus().equals("1"))
            return "fail: 모집 중이 아닙니다.";

        Puser puser = new Puser();
        puser.setUser(userRepository.findByUserServiceId(userServiceId).get());
        puserRepository.save(puser);
        party.addPuser(puser);

        party.setCurPlayer(party.getCurPlayer() + 1);

        // 파티 상태 수정
        if(party.getMaxPlayer() == party.getCurPlayer())
            party.setStatus("2");
        else
            party.setStatus("1");

        partyRepository.save(party);

        return "success";
    }

    @Override
    public String memberLeave(Long partyId, String userServiceId) {
        Party party = partyRepository.findByPartyId(partyId).orElse(null);

        // 오류: 유효하지 않은 사용자 아이디입니다.
        if(!userRepository.findByUserServiceId(userServiceId).isPresent())
            return "fail: 유효하지 않은 사용자 아이디입니다.";
        // 오류: 파티 상태
        if(party.getStatus().equals("3") || party.getStatus().equals("4")|| party.getStatus().equals("5"))
            return "fail: 파티를 탈퇴할 수 없습니다.";

        Puser thisuser = null;
        for (Puser puser: party.getPusers()) {
            if(puser.getUser().getUserServiceId().equals(userServiceId)) {
                thisuser = puser;
            }
        }

        // 오류: 파티에 없음
        if(thisuser == null)
            return "fail: 유저가 해당 파티의 파티원이 아닙니다.";
        // 오류: 파티장 탈퇴
        if(thisuser.isLeader())
            return "fail: 파티장은 파티를 탈퇴할 수 없습니다.";

        party.getPusers().remove(thisuser);
        puserRepository.delete(thisuser);

        party.setCurPlayer(party.getCurPlayer() - 1);

        // 파티 상태 수정
        if(party.getMaxPlayer() == party.getCurPlayer())
            party.setStatus("2");
        else
            party.setStatus("1");

        partyRepository.save(party);

        return "success";
    }

    @Override
    public boolean closeParty(Long partyId) {
        if(partyRepository.findByPartyId(partyId) == null)
            return false;

        Party party = partyRepository.findByPartyId(partyId).orElse(null);
        if(party.getStatus().equals("1"))
            party.setStatus("2");
        else if(party.getStatus().equals("2"))
            party.setStatus("1");
        partyRepository.save(party);
        return true;
    }
}
