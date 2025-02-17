package edu.du._waxing_home.review.domain;

import edu.du._waxing_home.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String title;
    private String content;
    private String author;
    private String password;
    private boolean best_review;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    // 조회수 필드 추가
    @Column(nullable = false)
    private Long views = 0L;  // 기본값 0으로 설정

    @Column(nullable = false)
    private String status = "N";

    @ElementCollection
    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();
}
