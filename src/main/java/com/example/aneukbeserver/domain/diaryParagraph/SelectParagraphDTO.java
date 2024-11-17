package com.example.aneukbeserver.domain.diaryParagraph;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelectParagraphDTO{
    private Long diary_id;
    private List<SaveDiaryParagraphDTO> content_list;
}
