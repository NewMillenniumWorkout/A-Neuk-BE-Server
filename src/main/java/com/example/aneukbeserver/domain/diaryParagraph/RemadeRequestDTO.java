package com.example.aneukbeserver.domain.diaryParagraph;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class RemadeRequestDTO {
    private Long diary_id;
    private Integer order_index;
    private String original_content;
    private List<String> emotions;
}
