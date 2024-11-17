package com.example.aneukbeserver.domain.diaryParagraph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemadeParagraphDTO {
    private Long paragraph_id;
    private Integer order_index;
    private String final_content;
}
