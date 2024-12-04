package com.example.aneukbeserver.domain.collection;

import com.example.aneukbeserver.domain.emotion.Emotion;
import com.example.aneukbeserver.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findByMember(Member member);

    Optional<Collection> findByMemberAndEmotion(Member member, Emotion emotion);
}
