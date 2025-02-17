package edu.du._waxing_home.review;

import edu.du._waxing_home.news.News;
import edu.du._waxing_home.reservation.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

//    @GetMapping("/list")
//    public String getReview(Model model) {
//        List<Review> review = reviewService.getAllReview(); // 리뷰 목록 가져오기
//        model.addAttribute("review", review); // 모델에 리뷰 목록 추가
//        return "pages/community/review"; // review.html 뷰로 이동
//    }

//     게시글 목록 보여줌
    @GetMapping("/listall")
    public String getReviewAll(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 15); // 한 페이지에 15개 항목
        Page<Review> reviewPage = reviewService.getAllReview(pageable);
        System.out.println("----------------"+reviewPage.getTotalElements());
        model.addAttribute("reviewPage", reviewPage);
        return "pages/admin/review/reviewlist"; // 템플릿 파일 이름
    }

    //     게시글 목록 보여줌 Y만
    @GetMapping("/list")
    public String getReview(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 15); // 한 페이지에 15개 항목
        Page<Review> reviewPage = reviewService.getAllReviewByStatus(pageable, "Y");
        System.out.println("----------------" + reviewPage.getTotalElements());
        model.addAttribute("reviewPage", reviewPage);
        return "pages/community/review"; // 템플릿 파일 이름
    }


    //전송 게시글 업로드
    @PostMapping("/submit")
    public String submitReview(Review review,
                               @RequestParam(value = "images", required = false) List<MultipartFile> images, // 파일 선택이 선택적
                               RedirectAttributes redirectAttributes) {
        try {
            // 이미지가 첨부되었다면 처리
            if (images != null && !images.isEmpty()) {
                reviewService.saveReviewWithImages(review, images);  // 여러 이미지를 서비스에서 처리
            } else {
                reviewService.saveReview(review);  // 이미지가 없으면 이미지 없이 저장
            }

            redirectAttributes.addFlashAttribute("message", "소식이 성공적으로 등록되었습니다.");
            return "redirect:/review/list"; // 소식 목록 페이지로 리다이렉트
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "소식 등록에 실패했습니다.");
            return "redirect:/review/write"; // 작성 페이지로 리다이렉트
        }
    }

    // 게시글 상세 보기
    @GetMapping("/detail/{id}")
    public String getNewsDetail(@PathVariable Long id, Model model) {

        // 뉴스 조회
        Review review = reviewService.getReviewById(id);

        // 조회수 증가
        reviewService.incrementViews(review);

        // 이전글과 다음글 조회
        Review previousReview = reviewService.getPreviousReview(id);
        Review nextReview = reviewService.getNextReview(id);

        // 모델에 뉴스 정보, 이전글, 다음글 추가
        model.addAttribute("review", review);
        model.addAttribute("previousReview", previousReview); // 이전글
        model.addAttribute("nextReview", nextReview); // 다음글

        // 상세 페이지로 이동
        return "pages/detail/detailreview";
    }

    // 게시글 수정 폼
    @GetMapping("/edit/{id}")
    public String editNews(@PathVariable Long id, Model model) {
        Review review = reviewService.getReviewById(id);
        model.addAttribute("review", review);
        return "pages/write/writereview";
    }

    // 게시글 수정
    @PostMapping("/update/{id}")
    public String updateReview(@PathVariable Long id, Review updatedReview,
                             @RequestParam(value = "images", required = false) List<MultipartFile> images,
                             RedirectAttributes redirectAttributes) {
        try {
            reviewService.updateReview(id, updatedReview, images);
            redirectAttributes.addFlashAttribute("message", "소식이 성공적으로 수정되었습니다.");
            return "redirect:/review/detail/" + id;
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "소식 수정에 실패했습니다.");
            return "redirect:/review/write/writereview/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "리뷰 삭제에 실패했습니다.");
        }
        return "redirect:/review/list";  // 리뷰 목록 페이지로 리다이렉트
    }

    // 예약 확정 처리
    @PostMapping("/confirm/{id}")
    public String confirmReview(@PathVariable("id") Long id) {
        // 예약 정보 가져오기
        Review review = reviewService.getReviewById(id);
        if (review != null) {
            // 예약 상태를 'y'로 업데이트 (확정)
            review.setStatus("y");
            reviewService.saveReview(review);  // 상태 저장
        }
        return "redirect:/review/listall";  // 예약 현황 페이지로 리다이렉트
    }


    // 예약 취소 처리
    @PostMapping("/cancel/{id}")
    public String cancelReview(@PathVariable("id") Long id) {
        // 예약 정보 가져오기
        Review review = reviewService.getReviewById(id);
        if (review != null) {
            // 예약 상태를 'n'으로 업데이트 (취소)
            review.setStatus("n");
            reviewService.saveReview(review);  // 상태 저장
        }
        return "redirect:/review/listall";  // 예약 현황 페이지로 리다이렉트
    }

}
