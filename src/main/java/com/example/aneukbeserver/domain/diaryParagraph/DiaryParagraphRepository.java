package com.example.aneukbeserver.domain.diaryParagraph;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryParagraphRepository extends JpaRepository<DiaryParagraph, Long> {
    Optional<DiaryParagraph> findByDiaryIdAndOrderIndex(Long diaryId, Integer orderIndex);
}
