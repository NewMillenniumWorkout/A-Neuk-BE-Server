package com.example.aneukbeserver.domain.selectedEmotion;

import com.example.aneukbeserver.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SelectedEmotionRepository extends JpaRepository<SelectedEmotion, Long> {
    @Query("SELECT se FROM SelectedEmotion se " +
           "JOIN FETCH se.emotion " +
           "JOIN se.diaryParagraph dp " +
           "JOIN dp.diary d " +
           "WHERE d.member = :member " +
           "AND d.createdDate BETWEEN :startDate AND :endDate")
    List<SelectedEmotion> findByDiaryParagraph_Diary_MemberAndDiaryParagraph_Diary_CreatedDateBetween(
            @Param("member") Member member,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
