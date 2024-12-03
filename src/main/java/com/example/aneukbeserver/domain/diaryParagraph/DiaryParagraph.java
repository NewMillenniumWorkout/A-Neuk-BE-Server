package com.example.aneukbeserver.domain.diaryParagraph;

import com.example.aneukbeserver.domain.diary.Diary;
import com.example.aneukbeserver.domain.selectedEmotion.SelectedEmotion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class DiaryParagraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    @Column
    private Integer orderIndex;

    @Column
    private String originalContent;

    @Column
    private String finalContent;

    @OneToMany(mappedBy = "diaryParagraph", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelectedEmotion> emotionList;
}
