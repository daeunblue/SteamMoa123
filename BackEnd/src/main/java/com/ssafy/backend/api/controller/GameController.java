package com.ssafy.backend.api.controller;

import com.ssafy.backend.api.service.GameService;
import com.ssafy.backend.api.response.GameDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 게임 관련 API 요청 처리를 위한 컨트롤러 정의.
 * http://localhost:8080/swagger-ui.html
 **/
@Api(value = "게임 API", tags = {"Game"})
@RestController
@RequestMapping("/api/games")
@Slf4j
public class GameController {
    @Autowired
    GameService gameService;


    @GetMapping("")
    @ApiOperation(value = "게임 리스트 전체 조회", notes = "전체 게임 리스트를 조회.")
    public ResponseEntity<?> getGameListAll(@RequestParam(required = false, defaultValue = "1") int page){
        JSONObject result = gameService.getGameList(page-1);
        return ResponseEntity.status(200).body(result);
    }


    @GetMapping("/search")
    @ApiOperation(value = "게임 리스트 조건 조회", notes = "검색어, 필터에 맞는 게임들을 조회.")
    public ResponseEntity<?> getGameListFiltered(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "") String name, @RequestParam(value = "tag", required = false, defaultValue = "") String[] tags){
        JSONObject result = gameService.searchGameList(page-1, name, tags);
        return ResponseEntity.status(200).body(result);
    }


    @GetMapping("/{gameid}")
    @ApiOperation(value="게임 상세 정보", notes = "gameid에 해당하는 게임 상세 정보를 조회한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "유효하지 않은 개체 id")
    })
    public ResponseEntity<?> getGameDetail(@PathVariable("gameid") Long gameid){
        GameDTO result = gameService.getGameDetail(gameid);
        if(result != null)
            return ResponseEntity.status(200).body(result);
        else
            return ResponseEntity.status(400).body(result);
    }
}
