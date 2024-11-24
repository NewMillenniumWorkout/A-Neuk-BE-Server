package com.example.aneukbeserver.domain.diary;

import com.example.aneukbeserver.domain.chat.Chat;
import com.example.aneukbeserver.domain.diaryParagraph.DiaryParagraph;
import com.example.aneukbeserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
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
    @Column(updatable = false)
    private LocalDate createdDate;

    @OneToMany(mappedBy = "diary")
    private List<DiaryParagraph> paragraphs;

    @Column(name = "image_url")
    private String imageUrl;
}
