package com.ssafy.backend.api.controller;

import com.ssafy.backend.api.service.NoticeService;
import com.ssafy.backend.api.response.NoticeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 알림 API 요청 처리를 위한 컨트롤러 정의.
 * http://localhost:8080/swagger-ui.html
 **/
@Api(value = "알림 API", tags = {"Notice"})
@RestController
@RequestMapping("/api/Notices")
@Slf4j
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    @PostMapping("")
    @ApiOperation(value = "알림 생성", notes = "**이 API는 개발 단계 테스트용입니다. 실제 알림은 스케줄러를 통해 자동으로 생성됩니다.** 알림을 생성합니다.")
    public ResponseEntity<?> createNotice(@RequestParam(required = false, defaultValue = "user_id") String userServiceId, @RequestParam(required = false, defaultValue = "64") Long partyId){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("message", noticeService.createNotice(userServiceId, partyId));

        return ResponseEntity.status(200).body(resultMap);
    }


    @GetMapping("/{userServiceId}")
    @ApiOperation(value = "알림 목록 조회", notes = "유저가 받은 알림을 모두 조회합니다. 현재는 파티 평가 알림만 발송.")
    public ResponseEntity<?> getNoticeList(@PathVariable("userServiceId") String userServiceId){
        Map<String,Object> resultMap = new HashMap<>();
        List<NoticeDTO> resultList = noticeService.getNoticeList(userServiceId);
        resultMap.put("notices", resultList);

        return ResponseEntity.status(200).body(resultMap);
    }

    @PutMapping("/{noticeId}")
    @ApiOperation(value = "알림 읽음 처리", notes = "특정 알림에 대해 읽음 처리를 합니다.")
    // @ApiIgnore Authentication authentication,
    public ResponseEntity<?> readNotice(@ApiIgnore Authentication authentication, @PathVariable("noticeId") Long noticeId){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("message", noticeService.readNotice(noticeId));

        return ResponseEntity.status(200).body(resultMap);
    }

    @DeleteMapping("/{noticeId}")
    @ApiOperation(value = "알림 삭제", notes = "특정 알림을 테이블에서 삭제합니다.")
    // @ApiIgnore Authentication authentication,
    public ResponseEntity<?> deleteNotice(@ApiIgnore Authentication authentication, @PathVariable("noticeId") Long noticeId){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("message", noticeService.deleteNotice(noticeId));

        return ResponseEntity.status(200).body(resultMap);
    }
}
