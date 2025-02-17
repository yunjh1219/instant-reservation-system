package edu.du._waxing_home.news;

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
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;


    //게시글 목록 보여줌
    @GetMapping("/list")
    public String getNews(Model model,
                          @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 15); // 한 페이지에 n개 항목
        Page<News> newsPage = newsService.getAllNews(pageable);

        model.addAttribute("newsPage", newsPage);
        return "pages/community/news";
    }


    //전송 게시글 업로드
    @PostMapping("/submit")
    public String submitReview(News news,
                               @RequestParam(value = "images", required = false) List<MultipartFile> images, // 파일 선택이 선택적
                               RedirectAttributes redirectAttributes) {
        try {
            // 이미지가 첨부되었다면 처리
            if (images != null && !images.isEmpty()) {
                newsService.saveNewsWithImages(news, images);  // 여러 이미지를 서비스에서 처리
            } else {
                newsService.saveNews(news);  // 이미지가 없으면 이미지 없이 저장
            }

            redirectAttributes.addFlashAttribute("message", "소식이 성공적으로 등록되었습니다.");
            return "redirect:/news/list"; // 소식 목록 페이지로 리다이렉트
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "소식 등록에 실패했습니다.");
            return "redirect:/news/write"; // 작성 페이지로 리다이렉트
        }
    }

    // 게시글 상세 보기
    @GetMapping("/detail/{id}")
    public String getNewsDetail(@PathVariable Long id, Model model) {

        // 뉴스 조회
        News news = newsService.getNewsById(id);

        // 조회수 증가
        newsService.incrementViews(news);

        // 이전글과 다음글 조회
        News previousNews = newsService.getPreviousNews(id);
        News nextNews = newsService.getNextNews(id);

        // 모델에 뉴스 정보, 이전글, 다음글 추가
        model.addAttribute("news", news);
        model.addAttribute("previousNews", previousNews); // 이전글
        model.addAttribute("nextNews", nextNews); // 다음글

        // 상세 페이지로 이동
        return "pages/detail/detailnews";
    }

    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            newsService.deleteNews(id);
            redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "리뷰 삭제에 실패했습니다.");
        }
        return "redirect:/news/list";  // 리뷰 목록 페이지로 리다이렉트
    }

    // 게시글 수정 폼
    @GetMapping("/edit/{id}")
    public String editNews(@PathVariable Long id, Model model) {
        News news = newsService.getNewsById(id);
        model.addAttribute("news", news);
        return "pages/write/writenews";
    }

    // 게시글 수정
    @PostMapping("/update/{id}")
    public String updateNews(@PathVariable Long id, News updatedNews,
                             @RequestParam(value = "images", required = false) List<MultipartFile> images,
                             RedirectAttributes redirectAttributes) {
        try {
            newsService.updateNews(id, updatedNews, images);
            redirectAttributes.addFlashAttribute("message", "소식이 성공적으로 수정되었습니다.");
            return "redirect:/news/detail/" + id;
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "소식 수정에 실패했습니다.");
            return "redirect:/news/write/writenews/" + id;
        }
    }

}