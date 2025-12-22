package com.example.aneukbeserver.domain.collection;

import com.example.aneukbeserver.domain.emotion.Emotion;
import com.example.aneukbeserver.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    @Query("SELECT c FROM Collection c JOIN FETCH c.emotion WHERE c.member = :member")
    List<Collection> findByMember(@Param("member") Member member);

    @Query("SELECT c FROM Collection c JOIN FETCH c.emotion WHERE c.member = :member AND c.emotion IN :emotions")
    List<Collection> findByMemberAndEmotionIn(@Param("member") Member member, @Param("emotions") List<Emotion> emotions);

    Optional<Collection> findByMemberAndEmotion(Member member, Emotion emotion);
}
