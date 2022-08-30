package com.ssafy.backend.api.service;

import com.ssafy.backend.api.request.UserRegisterPostReq;
import com.ssafy.backend.api.request.UserUpdatePutReq;
import com.ssafy.backend.api.response.UserDto;
import com.ssafy.backend.api.response.UserpageCompletedPartylistDTO;
import com.ssafy.backend.db.entity.party.Pvote;
import com.ssafy.backend.db.entity.user.UserTag;
import com.ssafy.backend.db.entity.follow.Follow;
import com.ssafy.backend.db.entity.party.Party;
import com.ssafy.backend.api.response.PartylistDTO;
import com.ssafy.backend.db.entity.party.Puser;
import com.ssafy.backend.db.repository.party.PvoteRepository;
import com.ssafy.backend.db.repository.user.UTagStorageRepository;
import com.ssafy.backend.db.repository.user.UserTagRepository;
import com.ssafy.backend.db.repository.follow.FollowRepository;
import com.ssafy.backend.db.repository.party.PartyRepository;
import com.ssafy.backend.db.repository.party.PuserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.backend.db.entity.user.User;
import com.ssafy.backend.db.repository.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTagRepository userTagRepository;

    @Autowired
    UTagStorageRepository uTagStorageRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PuserRepository puserRepository;

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    PvoteRepository pvoteRepository;

    @Override
    @Transactional
    public int createUser(UserRegisterPostReq userRegisterInfo) {
        User user = new User();

        boolean existUser = userRepository.existsByUserServiceId(userRegisterInfo.getUser_service_id());
        if(existUser) return 1; // 이미 존재하는 사용자명으로 생성 시도

        // 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장. -> 추후에 SpringSecurity 적용하고 사용해야함
        user.setPassword(passwordEncoder.encode(userRegisterInfo.getUser_service_pw()));

        System.out.println("인코더"+passwordEncoder.encode(userRegisterInfo.getUser_service_pw()));
        System.out.println("user 내부"+user.getPassword());
        boolean serviceIdValidate = checkServiceIdDuplicate(userRegisterInfo.getUser_service_id());
        boolean steamIdValidate = checkSteamIdDuplicate(userRegisterInfo.getUser_steam_id());

        if(!steamIdValidate && !serviceIdValidate){ // 두 경우를 다르게 볼것인지 프론트와 합의해야함 (코드 중복이 생기므로 이 부분도 개선해야함)
            user.setUserSteamId(userRegisterInfo.getUser_steam_id());
            user.setUserServiceId(userRegisterInfo.getUser_service_id());
            user.setUserPoint(36.5);
        }else{
            System.out.println("아이디 중복!");
            return 2;
        }

        user.setUserName(userRegisterInfo.getUser_name());
        userRepository.save(user);

        return 3;

    }

    @Override
    @Transactional
    public Map<String,Object> getUserInfoByUserId(String userServiceId) {
        Map<String, Object> result = new HashMap<>();
        // 디비에 유저 정보 조회 (userId 를 통한 조회).
        User user = userRepository.findByUserServiceId(userServiceId).get();
        if(user.getUserId() == null){
            System.out.println("아이디 에 해당하는 사용자 없음");
            result.put("message","Fail");
            return result;
        }else{
            if(user.getIsDeleted()){
                result.put("message","이미 탈퇴한 사용자의 정보입니다.");
            }else{
                UserDto userDto = new UserDto();
                userDto.setUserId(user.getUserId());
                userDto.setUserServiceId(user.getUserServiceId());
                userDto.setUserPoint(Math.round(user.getUserPoint()*10)/10.0);
                for (UserTag tag:user.getUTagLists()) {
                    userDto.addUserTags(tag.getUTagStorage().getContent());
                }
                userDto.setUserName(user.getUserName());
                result.put("user",userDto);
                result.put("message","Success");
            }
        }

        return result;
    }

    @Override
    public User getUserByUserId(String userId) {
        User user = userRepository.findByUserServiceId(userId).get();
        return user;
    }

    @Override
    @Transactional
    public boolean checkSteamIdDuplicate(String userSteamId) {
        boolean result = userRepository.existsByUserSteamId(userSteamId);// 스팀 아이디 중복체크 -> false : 회원가입 가능

        if(result){
            // 회원 가입시 -> 같은 스팀 아이디를 가졌던 사람중에 이전에 가입했었고 + 탈퇴한 회원인 경우 가입 가능하게
            if(userRepository.findByUserSteamIdAndIsDeleted(userSteamId,true).isPresent())
                return false;
        }


        return result;
    }

    @Override
    @Transactional
    public boolean checkServiceIdDuplicate(String userServiceId) {
        // 서비스 아이디 중복체크 -> false : 회원가입 불가능
        /* 추가해야하는 기능 : 글자 + 숫자 + 특수문자 유효성 검사 기능 추가해야함 (비밀번호에도 적용)*/
        boolean result = userRepository.existsByUserServiceId(userServiceId);
        return result;
    }

    @Override
    public boolean isEqualUserIdPw(String serviceId, String servicePw) {

        User user = userRepository.findByUserServiceId(serviceId).get();
//        if(user.getPassword());
        return false;
    }

    @Override
    @Transactional
    public boolean deleteUser(String userServiceId) {
        try{
            User user = userRepository.findByUserServiceId(userServiceId).get();
            user.setIsDeleted(true);
            userRepository.save(user);
//            userRepository.delete(user);
            System.out.println("사용자 삭제 요청 성공!");
            return true;
        }catch (Exception e){
            System.out.println("존재하지 않는 사용자 삭제 요청 -> 에러임!");
            return false;
        }
    }



    @Override
    @Transactional
    public boolean updateUser(Long userId, UserUpdatePutReq userUpdatePutReq) {
        try{
            User user = userRepository.findById(userId).get();

            // userName 수정
            user.setUserName(userUpdatePutReq.getUserName());

            // userTags 수정
            for (UserTag ut: user.getUTagLists()) {
                userTagRepository.delete(ut);
            }
            user.setUTagLists(new ArrayList<>());

            UserTag userTag;
            for (String tag:userUpdatePutReq.getUserTags()) {
                userTag = new UserTag();
                userTag.setUTagStorage(uTagStorageRepository.findById(Long.parseLong(tag)).get());
                userTagRepository.save(userTag);
                System.out.println(userTag.getUTagStorage().getContent());
                user.addUTagLists(userTag);
            }
            // password 수정 (-1 : 변경안함, 나머지값: 변경함)
            String password = userUpdatePutReq.getUserPassword();

            if(!password.equals("-1")){
                System.out.println("비밀번호도 수정함");
                user.setPassword(passwordEncoder.encode(userUpdatePutReq.getUserPassword()));
            }
            userRepository.save(user);
            System.out.println(user.getUserName());
            System.out.println("User 수정 요청 성공");

            return true;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("User 수정 요청 실패");
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateUserScore(Long partyId, String voterId, Long userId, Integer score) {
//        try{
//
//        }catch (Exception e){
//
//        }

        // 예외처리
        for(Pvote pv : (List<Pvote>) pvoteRepository.findAllByPartyId(partyId).orElse(Collections.EMPTY_LIST)) {
            if (pv.getVoterId() == userRepository.findByUserServiceId(voterId).get().getUserId() && pv.getUserServiceId().equals(userRepository.findByUserId(userId).get().getUserServiceId())) {
                return false;
            }
        }

        Optional<User> oUser = userRepository.findByUserId(userId);
        if(oUser.isPresent()){
            User user = userRepository.findByUserId(userId).get();
            Double curPoint = user.getUserPoint();
            /* 1~5까지 평가 점수가 들어오면 1(매우 나쁨) ~ 3(평균) ~ 5(매우 좋음) 각 단계를 0.1도씩 차등을 두어 처리*/
            switch (score){
                case 1:
                    curPoint -= 0.2;
                    break;
                case 2:
                    curPoint -= 0.1;
                    break;
                case 3:
                    break;
                case 4:
                    curPoint += 0.1;
                    break;
                case 5:
                    curPoint += 0.2;
                    break;
            }
            user.setUserPoint(curPoint);
            userRepository.save(user);
            System.out.println("현재 User의 온도: "+user.getUserPoint());
            return true;
        }else{
            return false;
        }

    }

    @Override
    @Transactional
    public boolean followUser(String followingUserId, String followerUserId) {
        boolean result = followRepository.existsByFollowerUserIdAndFollowingUserId(followerUserId, followingUserId);
        System.out.println("result : "+ result);

        // 이전 동일한 구독 이력이 있는지 확인
        // 있다면 -> 그냥 반환
        // 없다면 -> 구독 테이블 업데이트
        if(result){
            System.out.println("이미 구독했습니다.");
            return false;
        }else{
            Follow follow = new Follow();
            follow.setFollowerUserId(followerUserId);
            follow.setFollowingUserId(followingUserId);
            followRepository.save(follow);
            return true;
        }

//        try{
//
//        }catch (NoSuchElementException e){
//
//        }
    }

    @Override
    @Transactional
    public boolean unFollowUser(String followingUserId, String follwerUserId) {
        boolean result = followRepository.existsByFollowerUserIdAndFollowingUserId(follwerUserId, followingUserId);

        if(result){
            Follow follow = followRepository.findByFollowerUserIdAndFollowingUserId(follwerUserId, followingUserId).get();
            followRepository.delete(follow);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public List<String> getFollower(String userServiceId) { // 현재 유저를 구독하고 있는 사람들의 목록
        List<Follow> result = new ArrayList<>();
        List<String> realResult = new ArrayList<>();

        result = followRepository.findAllByFollowingUserId(userServiceId).get();

        for (Follow follow: result) {
            Optional<User> user = userRepository.findByUserServiceId(follow.getFollowerUserId());
            if(user.isPresent()){
                if(!user.get().getIsDeleted()){    // 탈퇴한 회원의 정보는 다 넘겨주지 않는다
                    System.out.println("진입");
                    System.out.println(follow.getFollowerUserId());
                    realResult.add(follow.getFollowerUserId());
                }
            }
        }
        return realResult;
    }

    @Override
    @Transactional
    public List<String> getFollowing(String userServiceId) {     // 현재 유저가 구독하고 있는 사람들의 목록
        List<Follow> result = new ArrayList<>();
        List<String> realResult = new ArrayList<>();

        result = followRepository.findAllByFollowerUserId(userServiceId).get();

        for (Follow follow: result) {
            Optional<User> user = userRepository.findByUserServiceId(follow.getFollowingUserId());
            if(user.isPresent()){
                if(!user.get().getIsDeleted()){    // 탈퇴한 회원의 정보는 다 넘겨주지 않는다
                    System.out.println("진입");
                    System.out.println(follow.getFollowerUserId());
                    realResult.add(follow.getFollowingUserId());
                }
            }
        }
        return realResult;
    }

    @Override
    public Map<String, Object> getMyPartiesProceeding(String userServiceId) {
        Map<String, Object> resultMap = new HashMap<>();
        List<PartylistDTO> parties = new ArrayList<>();

        List<Puser> pusers = puserRepository.findAllByUserOrderByPuserIdDesc(userRepository.findByUserServiceId(userServiceId).get()).orElse(Collections.EMPTY_LIST);
        for (Puser p: pusers) {
            Party party_temp = partyRepository.findByPusersContains(p).orElse(null);
            String partyStatus_temp = party_temp.getStatus();
            if(partyStatus_temp.equals("1") || partyStatus_temp.equals("2") ||partyStatus_temp.equals("3"))
                parties.add(new PartylistDTO(party_temp));
        }

        resultMap.put("parties", parties);
        return resultMap;
    }

    @Override
    public Map<String, Object> getMyPartiesCompleted(String userServiceId) {
        Map<String, Object> resultMap = new HashMap<>();
        List<UserpageCompletedPartylistDTO> parties = new ArrayList<>();

        List<Puser> pusers = puserRepository.findAllByUserOrderByPuserIdDesc(userRepository.findByUserServiceId(userServiceId).get()).orElse(Collections.EMPTY_LIST);
        for (Puser p: pusers) {
            Party party_temp = partyRepository.findByPusersContains(p).orElse(null);
            String partyStatus_temp = party_temp.getStatus();
            if(partyStatus_temp.equals("4") ||partyStatus_temp.equals("5")) {

                Set<Long> votingMembers = new HashSet<>();
                for(Pvote pvote: (List<Pvote>) pvoteRepository.findAllByPartyId(p.getParty().getPartyId()).orElse(Collections.EMPTY_LIST)){
                    votingMembers.add(pvote.getVoterId());
                }
                if(votingMembers.contains(p.getUser().getUserId()))
                    parties.add(new UserpageCompletedPartylistDTO(party_temp, true));
                else
                    parties.add(new UserpageCompletedPartylistDTO(party_temp, false));
            }
        }

        resultMap.put("parties", parties);
        return resultMap;
    }

    @Override
    public Map<String, Object> getMyPartiesCreated(String userServiceId) {
        Map<String, Object> resultMap = new HashMap<>();
        List<PartylistDTO> parties = new ArrayList<>();

        List<Puser> pusers = puserRepository.findAllByUserOrderByPuserIdDesc(userRepository.findByUserServiceId(userServiceId).get()).orElse(Collections.EMPTY_LIST);
        for (Puser p: pusers) {
            System.out.println(p.getUser().getUserServiceId());
            if(p.isLeader()) {
                parties.add(new PartylistDTO(partyRepository.findByPusersContains(p).orElse(null)));
            }
        }

        resultMap.put("parties", parties);
        return resultMap;
    }


}

