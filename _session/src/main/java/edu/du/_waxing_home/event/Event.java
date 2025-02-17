package edu.du._waxing_home.event;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@ToString
public class Event {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자 필드 추가
    @Column(nullable = true)
    private String author;  // 작성자, 널값 가능

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;


    // 이벤트 진행 기간 필드 추가
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate startDate;  // 시작 날짜
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate endDate;    // 종료 날짜

    @ElementCollection
    @CollectionTable(name = "event_images", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt; // 수정 일시

    // 조회수 필드 추가
    @Column(nullable = false)
    private Long views = 0L;  // 기본값 0으로 설정


}