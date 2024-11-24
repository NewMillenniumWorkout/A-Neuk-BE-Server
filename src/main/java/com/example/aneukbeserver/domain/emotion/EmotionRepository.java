package com.example.aneukbeserver.domain.emotion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    Emotion findByTitle(String title);
}
