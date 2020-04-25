package org.projects.polyglot.core.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "examples")
public class Example {

    @Id
    @GeneratedValue(generator="examples_seq")
    @SequenceGenerator(name="examples_seq",sequenceName="EXAMPLES_SEQ", allocationSize=1)
    private Integer id;

    @Column(nullable = false)
    private String example;

    @ManyToOne
    @JoinColumn(name = "WORD_ID", nullable = false)
    private Word word;

}
