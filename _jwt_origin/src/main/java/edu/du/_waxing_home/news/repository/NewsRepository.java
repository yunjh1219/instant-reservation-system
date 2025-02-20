package edu.du._waxing_home.news.repository;

import edu.du._waxing_home.news.domain.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    // createdAt 기준 내림차순으로 정렬(최신순이 위로)
    Page<News> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Optional<News> findTopByIdLessThanOrderByIdDesc(Long id);
    Optional<News> findTopByIdGreaterThanOrderByIdAsc(Long id);

}
