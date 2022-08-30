package com.ssafy.backend.api.controller;

import com.ssafy.backend.api.service.IntegratedSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 통합검색 API 요청 처리를 위한 컨트롤러 정의.
 * http://localhost:8080/swagger-ui.html
 **/
@Api(value = "통합검색 API", tags = {"IntegratedSearch"})
@RestController
@RequestMapping("/api")
@Slf4j
public class IntegratedSearchController {
    @Autowired
    IntegratedSearchService integratedSearchService;

    @GetMapping("/search")
    @ApiOperation(value = "통합 검색", notes = "type: user -> 유저 정보 검색 \n type: content -> '게임 이름'을 받아서 파티, 게임, 공략글을 검색")
    public ResponseEntity<?> getIntegratedSearch(@RequestParam(required = true, defaultValue = "content") String type, @RequestParam(required = false, defaultValue = "lucky") String keyword){
        Map<String,Object> result = integratedSearchService.getIntegratedSearch(type, keyword);
        return ResponseEntity.status(200).body(result);
    }

}
