package com.example.aneukbeserver.domain.emotion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmotionResponseDTO {
    private Long emotion_id;
    private String emotion_name;
    private String category;
    private String description;
}
