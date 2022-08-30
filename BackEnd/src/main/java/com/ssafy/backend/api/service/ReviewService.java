package com.ssafy.backend.api.service;

import com.ssafy.backend.api.request.ReviewPostReq;
import com.ssafy.backend.api.response.ReviewDto;
import com.ssafy.backend.db.entity.review.Review;

import java.util.List;

public interface ReviewService {
    boolean createOrUpdateReview(ReviewPostReq reviewPostReq);

    List<ReviewDto> findReviewByUserServiceId(String userServiceId);

    List<ReviewDto> findReviewByGameId(Long gameId);

    ReviewDto findeReviewByUserServiceIdAndGameId(String userServiceId, Long gameId);

    boolean existReview(Long reviewId);

    boolean deleteReview(Long reviewId);

}
