package org.projects.polyglot.core.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "words")
public class Word {

    @Id
    @GeneratedValue(generator="words_seq")
    @SequenceGenerator(name="words_seq",sequenceName="WORDS_SEQ", allocationSize=1)
    private Integer id;

    @Column(nullable = false)
    private String word;

    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Languages language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WordType wordType;

    @ManyToMany
    @JoinTable(
        name = "TRANSLATIONS",
        joinColumns = @JoinColumn(name = "TRANSLATION_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "WORD_ID", referencedColumnName = "ID")
    )
    private List<Word> translations;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL)
    private List<Example> examples;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL)
    private List<Property> properties;

}
