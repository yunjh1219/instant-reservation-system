package edu.du._waxing_home.news.controller;

import edu.du._waxing_home.news.domain.News;
import edu.du._waxing_home.news.service.NewsService;
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
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/news/list")
    public String getNews(Model model,
                          @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 15); // 한 페이지에 n개 항목
        Page<News> newsPage = newsService.getAllNews(pageable);

        model.addAttribute("newsPage", newsPage);
        return "pages/community/news";
    }

}
