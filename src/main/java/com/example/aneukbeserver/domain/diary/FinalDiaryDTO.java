package com.example.aneukbeserver.domain.diary;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinalDiaryDTO {
    private Long diary_id;
    private LocalDate date;
    private String content;

}
