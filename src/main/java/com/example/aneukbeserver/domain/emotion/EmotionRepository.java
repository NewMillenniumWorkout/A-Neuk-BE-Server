package com.example.aneukbeserver.domain.emotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    Optional<Emotion> findByTitle(String title);

    @Query("select e.category as category, count(e) as count from Emotion e group by e.category")
    List<CategoryCount> countByCategoryGrouped();

    interface CategoryCount {
        EmotionCategory getCategory();
        long getCount();
    }
}
