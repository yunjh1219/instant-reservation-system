package edu.du._waxing_home.news.service;

import edu.du._waxing_home.news.domain.News;
import edu.du._waxing_home.news.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;



    //이전글
    public News getPreviousNews(Long id) {
        return newsRepository.findTopByIdLessThanOrderByIdDesc(id).orElse(null);
    }
    //다음글
    public News getNextNews(Long id) {
        return newsRepository.findTopByIdGreaterThanOrderByIdAsc(id).orElse(null);
    }

    // 게시판 목록 불러오기
    public Page<News> getAllNews(Pageable pageable) {
        return newsRepository.findAllByOrderByCreatedAtDesc(pageable);
    }


}
