package edu.du._waxing_home.event.domain;

import edu.du._waxing_home.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate startDate;  // 시작 날짜
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate endDate;    // 종료 날짜

    private String status;
    private Long views = 0L;  // 기본값 0으로 설정
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ElementCollection
    @CollectionTable(name = "event_images", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @Builder
    public Event(User user, String title, String content, LocalDate startDate, LocalDate endDate, String status, Long views, List<String> imageUrls) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.views = views;
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>(); // null이 아닌 경우만 추가
    }

}
