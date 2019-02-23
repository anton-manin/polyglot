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
public class Word {

    @Id
    @GeneratedValue(generator="word_seq")
    @SequenceGenerator(name="word_seq",sequenceName="WORD_SEQ", allocationSize=1)
    private Integer id;

    @Column(nullable = false)
    private String word;

    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Languages language;

    @ManyToMany
    @JoinTable(
        name = "TRANSLATION",
        joinColumns = @JoinColumn(name = "TRANSLATION_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "WORD_ID", referencedColumnName = "ID")
    )
    private List<Word> translations;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL)
    private List<Example> examples;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL)
    private List<Property> properties;

}
