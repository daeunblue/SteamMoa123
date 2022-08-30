package com.ssafy.backend.api.controller;

import com.ssafy.backend.api.request.ReviewPostReq;
import com.ssafy.backend.api.request.TacticPostReq;
import com.ssafy.backend.api.response.ReviewDto;
import com.ssafy.backend.api.service.ReviewService;
import com.ssafy.backend.db.entity.review.Review;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "리뷰 API", tags = {"Review"})
@RestController
@RequestMapping("/api/game/reviews")
@Slf4j
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping()
    @ApiOperation(value="게임 리뷰 생성", notes = "리뷰 생성")
    // @ApiIgnore Authentication authentication,
    public ResponseEntity<? extends Map<String,Object>> createReview(@ApiIgnore Authentication authentication, @RequestBody ReviewPostReq reviewPostReq){
        Map<String,Object> resultMap = new HashMap<>();

        if(reviewService.createOrUpdateReview(reviewPostReq)){
            resultMap.put("msg","Success");
            return ResponseEntity.status(200).body(resultMap);
        }else{
            resultMap.put("msg","Fail");
            return ResponseEntity.status(400).body(resultMap);
        }
    }

    @PutMapping()
    @ApiOperation(value="게임 리뷰 수정", notes = "리뷰 수정")
    // @ApiIgnore Authentication authentication,
    public ResponseEntity<? extends Map<String,Object>> updateReview(@ApiIgnore Authentication authentication, @RequestBody ReviewPostReq reviewPostReq){
        Map<String,Object> resultMap = new HashMap<>();

        if(reviewService.createOrUpdateReview(reviewPostReq)){
            resultMap.put("message","Success");
            return ResponseEntity.status(200).body(resultMap);
        }else{
            resultMap.put("message","Fail");
            return ResponseEntity.status(400).body(resultMap);
        }
    }

    @GetMapping("/{user_service_id}")
    @ApiOperation(value = "유저 서비스 아이디에 해당하는 리뷰 반환", notes = "user_service_id에 대한 리뷰 정보 반환")
    public ResponseEntity<? extends Map<String,Object>> findReviewByUserServiceId(@PathVariable("user_service_id")String userServiceId){
        Map<String,Object> resultMap = new HashMap<>();

        List<ReviewDto> result = reviewService.findReviewByUserServiceId(userServiceId);

        if(result.size() == 0){
            resultMap.put("message","Fail, 조회 결과 없음");
            resultMap.put("status",406);
            return ResponseEntity.status(200).body(resultMap);
        }else{
            resultMap.put("message","조회 성공");
            resultMap.put("reviews", result);
            return ResponseEntity.status(200).body(resultMap);
        }
    }

    @GetMapping("/game/{game_id}")
    @ApiOperation(value = "게임에 해당하는 리뷰 반환", notes = "game_id에 대한 리뷰 정보 반환")
    public ResponseEntity<? extends Map<String,Object>> findReviewByGameId(@PathVariable("game_id")Long gameId){
        Map<String,Object> resultMap = new HashMap<>();


        List<ReviewDto> result = reviewService.findReviewByGameId(gameId);

        if(result.size() == 0){
            resultMap.put("message","Fail, 조회 결과 없음");
            resultMap.put("status",406);
            return ResponseEntity.status(200).body(resultMap);
        }else{
            resultMap.put("message","Success");
            resultMap.put("reviews", result);
            return ResponseEntity.status(200).body(resultMap);
        }
    }

    @GetMapping("/{user_service_id}/{game_id}")
    @ApiOperation(value = "유저의 리뷰 작성 여부 반환", notes = "user_service_id/game_id에 대한 리뷰 정보 반환")
    public ResponseEntity<? extends Map<String,Object>> findReviewByUserServiceIdAndGameId(@PathVariable("user_service_id")String userServiceId, @PathVariable("game_id")Long gameId){
        Map<String,Object> resultMap = new HashMap<>();

        ReviewDto reviewDto = reviewService.findeReviewByUserServiceIdAndGameId(userServiceId,gameId);


        if(reviewDto.getReviewId() == null){
            resultMap.put("message","Fail");
            resultMap.put("status",406);
            return ResponseEntity.status(200).body(resultMap);
        }else{
            resultMap.put("message","조회 성공");
            resultMap.put("review", reviewDto);
            return ResponseEntity.status(200).body(resultMap);
        }
    }


    @DeleteMapping("/{review_id}")
    @ApiOperation(value = "리뷰 삭제", notes = "review_id에 해당하는 리뷰글 삭제")
    // @ApiIgnore Authentication authentication,
    public ResponseEntity<? extends Map<String,Object>> deleteReview(@ApiIgnore Authentication authentication, @PathVariable("review_id")Long reviewId){
        Map<String, Object> resultMap = new HashMap<>();

        if(reviewService.existReview(reviewId)){    // 해당 리뷰글이 존재하는지 확인
            reviewService.deleteReview(reviewId);
            resultMap.put("message", "Success");
            return ResponseEntity.status(200).body(resultMap);
        }else{
            resultMap.put("message","Fail, 존재하지 않는 리뷰입니다.");
            return ResponseEntity.status(400).body(resultMap);
        }

    }

}
