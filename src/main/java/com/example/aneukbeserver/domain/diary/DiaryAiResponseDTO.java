package com.example.aneukbeserver.domain.diary;

import com.example.aneukbeserver.domain.diaryParagraph.SaveDiaryParagraphDTO;
import lombok.Data;

import java.util.List;

@Data
public class DiaryAiResponseDTO {
    private Long chat_id;
    private List<SaveDiaryParagraphDTO> content_list;
}
