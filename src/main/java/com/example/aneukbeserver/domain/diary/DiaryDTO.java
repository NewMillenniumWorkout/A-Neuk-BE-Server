package com.example.aneukbeserver.domain.diary;

import com.example.aneukbeserver.domain.emotion.Emotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryDTO {
    private Long diary_id;
    private LocalDate date;
    private String content;
    private String imageUrl;
    private List<Emotion> emotionList;
}
