package edu.du._waxing_home.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private LocalDate startDate;    // 이벤트 시작 날짜
    private LocalDate endDate;      // 이벤트 종료 날짜
    private List<MultipartFile> images = new ArrayList<>();  // 기본값을 빈 리스트로 설정
}
