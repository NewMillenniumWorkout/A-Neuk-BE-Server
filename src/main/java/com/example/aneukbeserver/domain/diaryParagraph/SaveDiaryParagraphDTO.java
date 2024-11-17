package com.example.aneukbeserver.domain.diaryParagraph;

import lombok.Data;

import java.util.List;

@Data
public class SaveDiaryParagraphDTO {
    private int order_index;
    private String original_content;
    private List<String> recommend_emotion;
}
