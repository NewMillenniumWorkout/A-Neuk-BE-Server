package com.example.aneukbeserver.domain.diary;

import com.example.aneukbeserver.domain.diaryParagraph.DiaryParagraph;
import com.example.aneukbeserver.domain.diaryParagraph.DiaryParagraphDTO;
import lombok.Data;

import java.util.List;

@Data
public class DiaryAiResponseDTO {
    private Long chat_id;
    private List<DiaryParagraphDTO> content_list;
}
