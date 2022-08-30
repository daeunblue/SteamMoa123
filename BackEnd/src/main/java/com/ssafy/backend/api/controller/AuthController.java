package com.ssafy.backend.api.controller;

import com.ssafy.backend.api.request.UserLoginPostReq;
import com.ssafy.backend.api.request.UserPwCheckPostReq;
import com.ssafy.backend.api.request.UserRegisterPostReq;
import com.ssafy.backend.api.response.UserLoginPostRes;
import com.ssafy.backend.api.response.UserPwCheckPostRes;
import com.ssafy.backend.api.service.UserService;
import com.ssafy.backend.common.model.response.BaseResponseBody;
import com.ssafy.backend.common.util.JwtTokenUtil;
import com.ssafy.backend.db.entity.user.User;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원가입 관련 API 요청 처리를 위한 컨트롤러 정의.
 * http://localhost:8080/swagger-ui.html
 **/
@Api(value = "유저 API", tags = {"SignUp"})
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping()
    @ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 409, message = "아이디 중복 오류"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends Map<String,Object>> register(
            @RequestBody @ApiParam(value="회원가입 정보", required = true)UserRegisterPostReq registerInfo) {
        Map<String, Object> result = new HashMap<>();
        //임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.
        int res = userService.createUser(registerInfo);

        if(res == 1){
            result.put("message", "Fail, 탈퇴한 회원의 아이디로 재 가입 불가능");
            return ResponseEntity.status(409).body(result);
        }else if(res == 2){
            result.put("message", "Fail, 스팀 아이디 중복");
            return ResponseEntity.status(409).body(result);
        }
        result.put("message","Success");
        return ResponseEntity.status(200).body(result);
    }


    @GetMapping("/signup/duplicated/{user_service_id}")
    @ApiOperation(value = "서비스 아이디 중복 검사", notes = "<strong>아이디</strong>를 통해 중복 체크.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 409, message = "서비스 아이디 중복 오류"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends BaseResponseBody> register(@PathVariable("user_service_id")String userServiceId) {
        boolean result = userService.checkServiceIdDuplicate(userServiceId); // 서비스 아이디 중복 검사
        if(result) return ResponseEntity.status(200).body(BaseResponseBody.of( 409, "서비스 아이디 중복"));
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "아이디 생성 가능"));
    }


    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "<strong>아이디, 비밀번호</strong>를 통해 로그인 유효성 확인.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 409, message = "서비스 아이디 중복 오류"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @ResponseBody
    public ResponseEntity<?>login(@RequestBody UserLoginPostReq userLoginPostReq){
        Map<String, Object> resultMap = new HashMap<>();

        String userId = userLoginPostReq.getUser_service_id();
        String password = userLoginPostReq.getUser_service_pw();

        boolean idExist = userService.checkServiceIdDuplicate(userId);
        // 아이디 자체가 유효하지 않은 경우 407에러 return
        if(!idExist){
            System.out.println("유효하지않은 로그인");
            resultMap.put("status",407);
            resultMap.put("message","Invalid Id");
            resultMap.put("accessToken",null);
            System.out.println(resultMap.get("status"));
//            return ResponseEntity.status(407).body(UserLoginPostRes.of(407, "Invalid Id", null));
            return ResponseEntity.status(407).body(UserLoginPostRes.of(407, "탈퇴한 사용자입니다.", null));
//            return ResponseEntity.status(407).body(resultMap);
        }


        User result = userService.getUserByUserId(userId);


        if(result.getIsDeleted()){  // 이미 탈퇴한 사용자의 경우
            return ResponseEntity.status(407).body(UserLoginPostRes.of(407, "탈퇴한 사용자입니다.", null));
        }

        if(passwordEncoder.matches(password, result.getPassword())) {
            // 유효한 패스워드가 맞는 경우, 로그인 성공으로 응답.(액세스 토큰을 포함하여 응답값 전달)
            return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", JwtTokenUtil.getToken(userId)));
        }

        // 유효하지 않는 패스워드인 경우, 로그인 실패로 응답.
        return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "Invalid Password", null));
    }

    @PostMapping("/check")
    @ApiOperation(value = "비밀번호 확인 ", notes = "<strong>아이디, 비밀번호</strong>를 통해 비밀번호 확인.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 409, message = "서비스 아이디 중복 오류"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @ResponseBody
    public ResponseEntity<? extends UserPwCheckPostRes>checkPassword(@RequestBody UserPwCheckPostReq userPwCheckPostReq){

        String userId = userPwCheckPostReq.getUser_service_id();
        String password = userPwCheckPostReq.getUser_service_pw();

        User result = userService.getUserByUserId(userId);
        if(passwordEncoder.matches(password, result.getPassword())) {
            // 유효한 패스워드가 맞는 경우, 성공으로 응답.
            return ResponseEntity.ok(UserPwCheckPostRes.of(200, "Success"));
        }

        // 유효하지 않는 패스워드인 경우, 실패로 응답.
        return ResponseEntity.ok(UserPwCheckPostRes.of(401, "Invalid Password"));
    }

    @DeleteMapping("/{user_service_id}")
    @ApiOperation(value = "회원 탈퇴", notes = "user_service_id에 해당하는 사용자 삭제")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends BaseResponseBody>deleteUser(@ApiIgnore Authentication authentication, @PathVariable("user_service_id") String userServiceId){
        boolean success = userService.deleteUser(userServiceId);
        if(success){
            return ResponseEntity.ok(BaseResponseBody.of(200,"Success"));
        }else{
            return ResponseEntity.ok(BaseResponseBody.of(400,"Fail"));
        }
    }








}
