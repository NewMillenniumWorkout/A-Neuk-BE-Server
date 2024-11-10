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

    @Column
    private String example;

    @Column(columnDefinition = "float8[]")
    private float[] embeddingVector;
}