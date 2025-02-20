package edu.du._waxing_home.review.controller;

import edu.du._waxing_home.review.domain.Review;
import edu.du._waxing_home.review.repository.ReviewRepository;
import edu.du._waxing_home.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/review/list")
    public String getReview(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 15); // 한 페이지에 15개 항목
        Page<Review> reviewPage = reviewService.getAllReviewByStatus(pageable, "Y");
        model.addAttribute("reviewPage", reviewPage);
        return "pages/community/review"; // 템플릿 파일 이름
    }

}
