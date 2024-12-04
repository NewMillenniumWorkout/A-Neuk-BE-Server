package com.example.aneukbeserver.domain.selectedEmotion;

import com.example.aneukbeserver.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SelectedEmotionRepository extends JpaRepository<SelectedEmotion, Long> {
    List<SelectedEmotion> findByDiaryParagraph_Diary_MemberAndDiaryParagraph_Diary_CreatedDateBetween(
            Member member,
            LocalDate startDate,
            LocalDate endDate);
}
