package com.ssafy.backend.api.service;

import com.ssafy.backend.api.request.UserRegisterPostReq;
import com.ssafy.backend.api.request.UserUpdatePutReq;
import com.ssafy.backend.db.entity.user.User;
import com.ssafy.backend.db.entity.follow.Follow;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserService {

    int createUser(UserRegisterPostReq userRegisterInfo);

    Map<String, Object> getUserInfoByUserId(String userServiceId);
    User getUserByUserId(String userId);

    // 유효성 검사
    boolean checkSteamIdDuplicate(String steamId);
    boolean checkServiceIdDuplicate(String serviceId);

    // 로그인 유효성 검사
    boolean isEqualUserIdPw(String serviceId, String servicePw);

    boolean deleteUser(String userServiceId);

    boolean updateUser(Long userId, UserUpdatePutReq userUpdatePutReq);

    // 매너온도 변경
    boolean updateUserScore(Long partyId, String voterId, Long userId, Integer score);

    // 팔로우 기능
    boolean followUser(String followingUserId, String followerUserId);
    boolean unFollowUser(String followingUserId, String followerUserId);
    List<String> getFollower(String userServiceId);
    List<String> getFollowing(String userServiceId);

    Map<String, Object> getMyPartiesProceeding(String userServiceId);
    Map<String, Object> getMyPartiesCompleted(String userServiceId);
    Map<String, Object> getMyPartiesCreated(String userServiceId);
}
