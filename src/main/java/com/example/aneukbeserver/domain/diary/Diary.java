package com.example.aneukbeserver.domain.diary;

import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.diaryParagraph.DiaryParagraph;
import com.example.aneukbeserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(mappedBy = "diary")
    private Chat chat;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "diary")
    private List<DiaryParagraph> paragraphs;
}
