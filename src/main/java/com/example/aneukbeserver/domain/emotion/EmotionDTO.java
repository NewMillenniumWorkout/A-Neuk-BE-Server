package com.example.aneukbeserver.domain.emotion;

import lombok.Data;

@Data
public class EmotionDTO {
    private Long id;
    private String title;
    private String category;
    private String example;
}
