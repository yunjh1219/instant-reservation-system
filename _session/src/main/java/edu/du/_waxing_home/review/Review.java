package edu.du._waxing_home.review;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@ToString
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Size(max = 10)
    private String name;

    @NotNull
    private String treatment;

    @NotNull
    @Pattern(regexp = "\\d{10,12}", message = "전화번호는 숫자만 입력합니다.")
    private String phoneNumber;

    @NotNull
    private String email;

    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ElementCollection
    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt; // 수정 일시

    // 조회수 필드 추가
    @Column(nullable = false)
    private Long views = 0L;  // 기본값 0으로 설정

    @Column(nullable = false)
    private String status = "N";

}
