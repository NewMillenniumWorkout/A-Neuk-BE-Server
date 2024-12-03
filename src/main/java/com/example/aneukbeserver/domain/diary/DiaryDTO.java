package com.example.aneukbeserver.domain.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryDTO {
    private Long diary_id;
    private LocalDate date;
    private String content;
    private String imageUrl;
}
