package com.example.aneukbeserver.domain.emotion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    Optional<Emotion> findByTitle(String title);
}
