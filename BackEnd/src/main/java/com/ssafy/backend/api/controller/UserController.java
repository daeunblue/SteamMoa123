package com.ssafy.backend.api.controller;

import com.ssafy.backend.api.request.UserFollowPostReq;
import com.ssafy.backend.api.request.UserUpdatePutReq;
import com.ssafy.backend.api.response.FollowDto;
import com.ssafy.backend.api.response.UserRes;
import com.ssafy.backend.api.service.UserService;
import com.ssafy.backend.common.auth.SsafyUserDetails;
import com.ssafy.backend.db.entity.user.User;
import com.ssafy.backend.api.response.UserDto;
import com.ssafy.backend.db.entity.user.UserTag;
import com.ssafy.backend.db.entity.follow.Follow;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 * http://localhost:8080/swagger-ui.html
 **/
@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;


    /* 토큰 인증 처리 필요 */
    @GetMapping("/profile/{user_service_id}")
    @ApiOperation(value = "회원 조회", notes = "<strong>사용자 아이디</strong>를 통해 회원 정보 조회.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 403, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends Map<String,Object>> getUserInfo(@PathVariable("user_service_id") String userServiceId) {

        Map<String, Object> result = new HashMap<>();

        result = userService.getUserInfoByUserId(userServiceId);
        if (result.get("message").equals("Fail")){
            return ResponseEntity.status(403).body(result);
        }else{
            return ResponseEntity.status(200).body(result);
        }


    }

    @PutMapping("/profile")
    @ApiOperation(value = "회원 정보 수정", notes = "<strong>회원 정보 수정 </strong>")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends Map<String, Object>> updateUserInfo(@ApiIgnore Authentication authentication, @RequestBody UserUpdatePutReq userUpdatePutReq) {
        /**
         * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
         * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
         */
        Map<String, Object> result = new HashMap<>();

        // (수정 필요) Service 단으로 넘겨서 처리해야하는 비즈니스 로직
        SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
        User user = userDetails.getUser();
        String userServiceId = user.getUserServiceId();
        userService.updateUser(user.getUserId(), userUpdatePutReq);
        result = userService.getUserInfoByUserId(userServiceId);


        result.put("message","회원 정보 업데이트 성공");
        return ResponseEntity.status(200).body(result);

    }

    // Authentication 추가해야하는 API
    @PostMapping("/follow")
    @ApiOperation(value = "유저 팔로우 추가", notes = "user_service_id에 해당하는 유저를 팔로우")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
//    public ResponseEntity<? extends Map<String, Object>> followUser(@ApiIgnore Authentication authentication, @RequestBody UserFollowPostReq userFollowPostReq){
    public ResponseEntity<? extends Map<String, Object>> followUser(@ApiIgnore Authentication authentication, @RequestBody UserFollowPostReq userFollowPostReq){

        Map<String, Object> resultMap = new HashMap<>();

        String FollowingUserId = userFollowPostReq.getFollowingUserId();
        String FollowUserId = userFollowPostReq.getFollowerUserId();
        boolean result = userService.followUser(FollowingUserId, FollowUserId);

        if(result){
            resultMap.put("message","Success");
            return ResponseEntity.status(200).body(resultMap);
        }else{
            resultMap.put("message","Fail");
            return ResponseEntity.status(400).body(resultMap);
        }
    }



    // Authentication 추가해야하는 API
    @DeleteMapping("/unfollow")
    @ApiOperation(value = "유저 팔로우 삭제", notes = "user_service_id에 해당하는 유저를 언팔로우")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 500, message = "서버 오류")
    })

//    public ResponseEntity<? extends Map<String, Object>> unFollowUser(@ApiIgnore Authentication authentication, @RequestBody UserFollowPostReq userFollowPostReq){
    public ResponseEntity<? extends Map<String, Object>> unFollowUser(@ApiIgnore Authentication authentication, @RequestBody UserFollowPostReq userFollowPostReq){
        Map<String, Object> resultMap = new HashMap<>();

        String FollowingUserId = userFollowPostReq.getFollowingUserId();
        String FollowUserId = userFollowPostReq.getFollowerUserId();
        boolean result = userService.unFollowUser(FollowingUserId, FollowUserId);

        if(result){
            resultMap.put("message","Success");
            return ResponseEntity.status(200).body(resultMap);
        }else{
            resultMap.put("message","Fail");
            return ResponseEntity.status(400).body(resultMap);
        }
    }

    @GetMapping("/follow/{user_service_id}")
    @ApiOperation(value = "유저 팔로잉 리스트 반환", notes = "user_service_id에 해당하는 유저가 팔로우하고 있는 사용자들의 목록 반환")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends Map<String, Object>> getUserFollowingList(@PathVariable("user_service_id")String userServiceId){
        Map<String, Object> resultMap = new HashMap<>();

        List<String> followList = userService.getFollowing(userServiceId);
        resultMap.put("followings",followList);
        resultMap.put("message","Success");

        return ResponseEntity.status(200).body(resultMap);
    }

    @GetMapping("/follower/{user_service_id}")
    @ApiOperation(value = "유저 팔로워 리스트 반환", notes = "user_service_id에 해당하는 유저를 팔로우하고 있는 사용자들의 목록 반환")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends Map<String, Object>> getUserFollowerLIst(@PathVariable("user_service_id")String userServiceId){
        Map<String, Object> resultMap = new HashMap<>();

        List<String> followList = userService.getFollower(userServiceId);
        resultMap.put("followers",followList);
        resultMap.put("message","Success");

        return ResponseEntity.status(200).body(resultMap);
    }

    @GetMapping("/profile/{user_service_id}/parties/proceeding")
    @ApiOperation(value = "내가 참여중인 파티", notes = "내가 생성하거나 참여한 파티 중 모집 중, 모집 완료, 플레이 중인 파티를 보여줍니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends Map<String, Object>> getMyPartiesProceeding(@ApiIgnore Authentication authentication, @PathVariable("user_service_id")String userServiceId){
        Map<String, Object> resultMap = userService.getMyPartiesProceeding(userServiceId);

        return ResponseEntity.status(200).body(resultMap);
    }

    @GetMapping("/profile/{user_service_id}/parties/completed")
    @ApiOperation(value = "내가 참여했고 이제 완료된 파티", notes = "내가 생성했거나 참여했던 파티 중 모집이 실패했거나 플레이가 종료된 파티를 보여줍니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends Map<String, Object>> getMyPartiesCompleted(@ApiIgnore Authentication authentication, @PathVariable("user_service_id")String userServiceId){
        Map<String, Object> resultMap = userService.getMyPartiesCompleted(userServiceId);

        return ResponseEntity.status(200).body(resultMap);
    }


    @GetMapping("/profile/{user_service_id}/parties/created")
    @ApiOperation(value = "내가 작성한 파티", notes = "내가 지금까지 생성한 모든 파티를 보여줍니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends Map<String, Object>> getMyPartiesCreated(@ApiIgnore Authentication authentication, @PathVariable("user_service_id")String userServiceId){
        Map<String, Object> resultMap = userService.getMyPartiesCreated(userServiceId);

        return ResponseEntity.status(200).body(resultMap);
    }
}

