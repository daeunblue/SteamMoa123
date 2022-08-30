package com.ssafy.backend.db.repository.review;

import com.ssafy.backend.db.entity.review.Review;
import com.ssafy.backend.db.entity.tactic.Tactic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByGameGameIdAndUserUserServiceId(Long gameId, String userServiceId);
    Optional<Review> findByUserUserServiceIdAndGameGameId(String userServiceId, Long gameId);
    Optional<List<Review>> findAllByUserUserServiceId(String userServiceId);

    Optional<List<Review>> findAllByGameGameId(Long gameId);

    boolean existsByReviewId(Long reviewId);

    Optional<Review> findByReviewId(Long reviewId);

}
