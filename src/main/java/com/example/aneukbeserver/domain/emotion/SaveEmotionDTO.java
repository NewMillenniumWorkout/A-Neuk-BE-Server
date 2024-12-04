package com.example.aneukbeserver.domain.emotion;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class SaveEmotionDTO {
    private Long diary_id;
    private Integer order_index;
    private List<String> emotions;
}
