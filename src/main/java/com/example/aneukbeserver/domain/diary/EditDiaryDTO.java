package com.example.aneukbeserver.domain.diary;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditDiaryDTO {
    private Long diary_id;
    private String content;
}
