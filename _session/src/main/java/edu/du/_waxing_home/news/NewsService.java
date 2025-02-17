package edu.du._waxing_home.news;

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
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    //Id로 찾기
    public News getNewsById(Long id) {
        return newsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid news ID"));
    }

    //이전글
    public News getPreviousNews(Long id) {
        return newsRepository.findTopByIdLessThanOrderByIdDesc(id).orElse(null);
    }
    //다음글
    public News getNextNews(Long id) {
        return newsRepository.findTopByIdGreaterThanOrderByIdAsc(id).orElse(null);
    }



    // 조회수 증가 메서드
    @Transactional
    public void incrementViews(News news) {
        // 조회수 증가
        news.setViews(news.getViews() + 1);

        // 업데이트된 뉴스 저장
        newsRepository.save(news);
    }

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";


    // 게시판 목록 불러오기
    public Page<News> getAllNews(Pageable pageable) {
        return newsRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // 이미지가 없는 경우
    public void saveNews(News news) {
        newsRepository.save(news);
    }

    // 이미지가 있는 경우
    public void saveNewsWithImages(News news, List<MultipartFile> images) throws IOException {
        // 이미지 저장 후 이미지 URL 목록을 반환
        List<String> imageUrls = saveImages(images);
        news.setImageUrls(imageUrls); // 이미지 URL을 뉴스 객체에 설정
        newsRepository.save(news); // 뉴스 객체와 이미지를 함께 저장
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
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        // 이미지 삭제
        for (String imageUrl : news.getImageUrls()) {
            String imagePath = "src/main/resources/static/" + imageUrl;  // 이미지 경로
            deleteImage(imagePath);  // 이미지 삭제 메서드 호출
        }

        newsRepository.delete(news);  // 리뷰 삭제
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
    public void updateNews(Long id, News updatedNews, List<MultipartFile> images) throws IOException {
        News news = getNewsById(id);
        news.setTitle(updatedNews.getTitle());
        news.setContent(updatedNews.getContent());

        if (images != null && !images.isEmpty()) {
            // 기존 이미지 삭제
            for (String imageUrl : news.getImageUrls()) {
                String imagePath = "src/main/resources/static" + imageUrl;  // 이미지 경로
                deleteImage(imagePath);  // 이미지 삭제 메서드 호출
            }

            // 새로운 이미지 저장
            List<String> imageUrls = saveImages(images);
            news.setImageUrls(imageUrls); // 이미지 URL을 뉴스 객체에 설정
        }

        newsRepository.save(news); // 뉴스 객체 저장
    }


}