package com.example.aneukbeserver.domain.emotion;

import lombok.Data;

@Data
public class EmotionDTO {
    private Long id;
    private String title;
    private EmotionCategory category;
    private String example;
    private String description;
}
