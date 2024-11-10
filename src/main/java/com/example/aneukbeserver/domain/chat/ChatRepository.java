package com.example.aneukbeserver.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByMemberId(Long memberId);

    Optional<Chat> findTopByMemberIdOrderByCreatedDateDesc(Long memberId);
}
