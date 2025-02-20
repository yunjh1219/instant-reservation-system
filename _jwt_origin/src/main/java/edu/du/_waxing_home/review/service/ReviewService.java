package edu.du._waxing_home.review.service;

import edu.du._waxing_home.review.domain.Review;
import edu.du._waxing_home.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    //이전글
    public Review getPreviousReview(Long id) {
        return reviewRepository.findTopByIdLessThanOrderByIdDesc(id).orElse(null);
    }
    //다음글
    public Review getNextReview(Long id) {
        return reviewRepository.findTopByIdGreaterThanOrderByIdAsc(id).orElse(null);
    }

    //등록 수락한 리뷰 리스트
    public Page<Review> getAllReviewByStatus(Pageable pageable, String status) {
        return reviewRepository.findByStatus(pageable, status);
    }

}
