package edu.du._waxing_home.review;


import edu.du._waxing_home.news.News;
import edu.du._waxing_home.reservation.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {


    @Autowired
    private ReviewRepository reviewRepository;


    //Id로 찾기
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid news ID"));
    }


    //이전글
    public Review getPreviousReview(Long id) {
        return reviewRepository.findTopByIdLessThanOrderByIdDesc(id).orElse(null);
    }
    //다음글
    public Review getNextReview(Long id) {
        return reviewRepository.findTopByIdGreaterThanOrderByIdAsc(id).orElse(null);
    }

    // 조회수 증가 메서드
    @Transactional
    public void incrementViews(Review review) {
        // 조회수 증가
        review.setViews(review.getViews() + 1);

        // 업데이트된 뉴스 저장
        reviewRepository.save(review);
    }




    // 게시판 목록 불러오기
    public Page<Review> getAllReview(Pageable pageable) {
        return reviewRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<Review> getAllReviewByStatus(Pageable pageable, String status) {
        return reviewRepository.findByStatus(pageable, status);
    }



    // 이미지 저장
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    // 이미지가 없는 경우
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    // 이미지가 있는 경우
    public void saveReviewWithImages(Review review, List<MultipartFile> images) throws IOException {
        // 이미지 저장 후 이미지 URL 목록을 반환
        List<String> imageUrls = saveImages(images);
        review.setImageUrls(imageUrls); // 이미지 URL을 뉴스 객체에 설정
        reviewRepository.save(review); // 뉴스 객체와 이미지를 함께 저장
    }

    // 이미지 저장 메서드
    private List<String> saveImages(List<MultipartFile> images) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile image : images) {
            // 이미지 파일 이름을 UUID로 변경하여 중복을 방지
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();

            // 이미지 파일을 서버 내 지정된 디렉터리에 저장
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent());  // 디렉터리가 없으면 생성
            image.transferTo(path);  // 이미지 파일을 지정된 경로로 저장

            // 웹에서 접근 가능한 이미지 경로를 URL 형식으로 생성하여 리스트에 추가
            imageUrls.add("/uploads/" + fileName);
        }

        return imageUrls;
    }

    // 게시글 삭제 메서드
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        // 이미지 삭제
        for (String imageUrl : review.getImageUrls()) {
            String imagePath = "src/main/resources/static/" + imageUrl;  // 이미지 경로
            deleteImage(imagePath);  // 이미지 삭제 메서드 호출
        }

        reviewRepository.delete(review);  // 리뷰 삭제
    }

    private void deleteImage(String imagePath) {
        Path path = Paths.get(imagePath);
        try {
            Files.deleteIfExists(path);  // 파일이 존재하면 삭제
        } catch (IOException e) {
            e.printStackTrace();  // 파일 삭제 오류 처리
        }
    }



    // 게시글 수정 메서드
    public void updateReview(Long id, Review updatedReview, List<MultipartFile> images) throws IOException {
        Review review = getReviewById(id);
        review.setTitle(updatedReview.getTitle());
        review.setContent(updatedReview.getContent());

        if (images != null && !images.isEmpty()) {
            // 기존 이미지 삭제
            for (String imageUrl : review.getImageUrls()) {
                String imagePath = "src/main/resources/static" + imageUrl;  // 이미지 경로
                deleteImage(imagePath);  // 이미지 삭제 메서드 호출
            }

            // 새로운 이미지 저장
            List<String> imageUrls = saveImages(images);
            review.setImageUrls(imageUrls); // 이미지 URL을 뉴스 객체에 설정
        }

        reviewRepository.save(review); // 뉴스 객체 저장
    }

    // 등록 상태를 'y' (등록)로 변경
    public Review confirmRegistration(Long id) {
        Review review = getReviewById(id);
        if (review != null) {
            review.setStatus("y");  // 상태를 'y'로 변경하여 등록
            reviewRepository.save(review);  // 상태 변경된 review를 저장
            return review;  // 변경된 review 반환
        }
        return null;
    }

    // 등록 상태를 'n' (미등록)으로 변경
    public Review cancelRegistration(Long id) {
        Review review = getReviewById(id);
        if (review != null) {
            review.setStatus("n");  // 상태를 'n'으로 변경하여 미등록 처리
            reviewRepository.save(review);  // 상태 변경된 review를 저장
            return review;  // 변경된 review 반환
        }
        return null;
    }


}
