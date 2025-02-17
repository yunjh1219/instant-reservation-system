package edu.du._waxing_home.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {



    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Optional<Review> findTopByIdLessThanOrderByIdDesc(Long id);
    Optional<Review> findTopByIdGreaterThanOrderByIdAsc(Long id);
    Page<Review> findByStatus(Pageable pageable, String status);

}
