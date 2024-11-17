package com.example.aneukbeserver.domain.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthDiaryDTO {
    private LocalDate month;
    private Long diary_id;
}
