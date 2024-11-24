package com.example.aneukbeserver.domain.diary;

import com.example.aneukbeserver.domain.diaryParagraph.SaveDiaryParagraphDTO;

import java.util.List;

public class DiaryFirstResponseDTO {
    private Long chat_id;
    private List<SaveDiaryParagraphDTO> content_list;
}
