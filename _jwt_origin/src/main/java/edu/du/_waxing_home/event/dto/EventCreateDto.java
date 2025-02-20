package edu.du._waxing_home.event.dto;

import edu.du._waxing_home.event.domain.Event;
import edu.du._waxing_home.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateDto {
    private String title;           // 이벤트 제목
    private String content;         // 이벤트 내용
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate startDate;    // 이벤트 시작 날짜
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate endDate;      // 이벤트 종료 날짜
    private List<MultipartFile> images;
    private Long views = 0L;        // 기본값 0L로 설정

    public Event toEntity(User user, List<String> imageUrls) {
        return Event.builder()
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .imageUrls(imageUrls)  // 이미지가 비어 있으면 null로 설정
                .views(views)           // views 필드도 설정
                .user(user)
                .build();
    }
}
