package com.example.aneukbeserver.domain.diaryParagraph;

import com.example.aneukbeserver.domain.emotion.EmotionDTO;
import lombok.Data;

import java.util.List;

@Data
public class DiaryParagraphDTO {
    private int order_index;
    private String original_content;
    private List<EmotionDTO> recommend_emotion;
}
