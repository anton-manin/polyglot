package org.projects.polyglot.core.domain;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "TRANSLATIONS",
        joinColumns = @JoinColumn(name = "TRANSLATION_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "WORD_ID", referencedColumnName = "ID")
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<Word> translations;

    @OneToMany(mappedBy = "word", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    @Fetch(FetchMode.SUBSELECT)
    private List<Example> examples;

    @OneToMany(mappedBy = "word", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    @Fetch(FetchMode.SUBSELECT)
    private List<Property> properties;

    public Word() {
        this.translations = new ArrayList<>();
        this.examples = new ArrayList<>();
        this.properties = new ArrayList<>();
    }

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
