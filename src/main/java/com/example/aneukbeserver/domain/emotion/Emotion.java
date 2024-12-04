package com.example.aneukbeserver.domain.emotion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Emotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    @Column(nullable = false)
    private EmotionCategory category;

    @Column
    private String description;

    @Column
    private String example;


}
