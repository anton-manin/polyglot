package org.projects.polyglot.core.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(id, word1.id) &&
                Objects.equals(word, word1.word) &&
                Objects.equals(priority, word1.priority) &&
                language == word1.language &&
                wordType == word1.wordType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, priority, language, wordType);
    }
}
