package com.ssafy.backend.api.service;

import com.ssafy.backend.api.request.ReviewPostReq;
import com.ssafy.backend.api.response.ReviewDto;
import com.ssafy.backend.db.entity.review.Review;
import com.ssafy.backend.db.repository.review.ReviewRepository;
import com.ssafy.backend.db.repository.user.UserRepository;
import com.ssafy.backend.db.repository.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository gameRepository;
    @Override
    @Transactional
    public boolean createOrUpdateReview(ReviewPostReq reviewPostReq) {
        Review review = new Review();
        Long gameId = reviewPostReq.getGameId();
        String userServiceId = reviewPostReq.getUserServiceId();

        Optional<Review> optionalReview = reviewRepository.findByGameGameIdAndUserUserServiceId(gameId, userServiceId);
        try{
            // 사용자가 이미 해당 게임에 리뷰를 작성했는지 확인하기.
            if(optionalReview.isPresent()){
                review = optionalReview.get();
                System.out.println("이미 해당 리뷰에 대해 작성했습니다.");
                review.setReviewContent(reviewPostReq.getReviewContent());
                review.setReviewScore(reviewPostReq.getReviewScore());
                reviewRepository.save(review);
            }else{
                System.out.println("작성한적 없음");
                review.setReviewContent(reviewPostReq.getReviewContent());
                review.setReviewScore(reviewPostReq.getReviewScore());
                review.setUser(userRepository.findByUserServiceId(reviewPostReq.getUserServiceId()).get());
                review.setGame(gameRepository.findByGameId(reviewPostReq.getGameId()).orElse(null));
                review.setLocalDateTime(LocalDateTime.now().plusHours(9));
                reviewRepository.save(review);
            }
            return true;
        }catch (Exception e){ // 예외처리 해야할 부분
            return false;
        }
    }

    @Override
    public List<ReviewDto> findReviewByUserServiceId(String userServiceId) {
        List<Review> list = new ArrayList<>();
        list = reviewRepository.findAllByUserUserServiceId(userServiceId).get();

        List<ReviewDto> customResult = new ArrayList<>();
        for (Review review : list) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setReviewId(review.getReviewId());
            reviewDto.setReviewContent(review.getReviewContent());
            reviewDto.setReviewScore(review.getReviewScore());
            reviewDto.setUserServiceId(review.getUser().getUserServiceId());
            reviewDto.setCurrentDate(review.getLocalDateTime());
            reviewDto.setGameId(review.getGame().getGameId());
            reviewDto.setGameName(review.getGame().getName());
            customResult.add(reviewDto);
        }
        return customResult;
    }

    @Override
    public List<ReviewDto> findReviewByGameId(Long gameId) {
        List<Review> list = new ArrayList<>();
        list = reviewRepository.findAllByGameGameId(gameId).get();

        List<ReviewDto> customResult = new ArrayList<>();
        for (Review review : list) {
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setReviewId(review.getReviewId());
            reviewDto.setReviewContent(review.getReviewContent());
            reviewDto.setReviewScore(review.getReviewScore());
            reviewDto.setUserServiceId(review.getUser().getUserServiceId());
            reviewDto.setCurrentDate(review.getLocalDateTime());
            reviewDto.setGameId(review.getGame().getGameId());
            reviewDto.setGameName(review.getGame().getName());
            customResult.add(reviewDto);
        }

        return customResult;
    }

    @Override
    @Transactional
    public ReviewDto findeReviewByUserServiceIdAndGameId(String userServiceId, Long gameId) {
        Review review = new Review();
        ReviewDto reviewDto = new ReviewDto();

        if(reviewRepository.findByUserUserServiceIdAndGameGameId(userServiceId, gameId).isPresent()){
            review = reviewRepository.findByUserUserServiceIdAndGameGameId(userServiceId, gameId).get();

            reviewDto.setReviewId(review.getReviewId());
            reviewDto.setReviewContent(review.getReviewContent());
            reviewDto.setReviewScore(review.getReviewScore());
            reviewDto.setCurrentDate(review.getLocalDateTime());
            reviewDto.setUserServiceId(review.getUser().getUserServiceId());
        }

        return reviewDto;
    }

    @Override
    public boolean existReview(Long reviewId) {
        boolean result = reviewRepository.existsByReviewId(reviewId);

        if(result) return true;
        else return false;

    }

    @Override
    @Transactional
    public boolean deleteReview(Long reviewId) {
        reviewRepository.delete(reviewRepository.findByReviewId(reviewId).get());
        try{
//            reviewRepository.delete(reviewRepository.findByReviewId(reviewId).get());
//            return true;
        }catch (Exception e){
            return false;
        }
        return true;
    }


}
