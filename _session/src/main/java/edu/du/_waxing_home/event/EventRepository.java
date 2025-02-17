package edu.du._waxing_home.event;

import edu.du._waxing_home.news.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {


    // createdAt 기준 내림차순으로 정렬(최신순이 위로)
    Page<Event> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Optional<Event> findTopByIdLessThanOrderByIdDesc(Long id);
    Optional<Event> findTopByIdGreaterThanOrderByIdAsc(Long id);

    // 현재 진행 중인 이벤트만 조회
    @Query("SELECT e FROM Event e WHERE e.endDate >= :currentDate ORDER BY e.createdAt DESC")
    Page<Event> findAllActiveEvents(LocalDate currentDate, Pageable pageable);

    // 종료된 이벤트를 반환하는 쿼리
    @Query("SELECT e FROM Event e WHERE e.endDate < :currentDate")
    Page<Event> findAllEndedEvents(@Param("currentDate") LocalDate currentDate, Pageable pageable);


}
