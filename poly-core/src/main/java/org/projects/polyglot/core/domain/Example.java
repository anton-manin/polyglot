package org.projects.polyglot.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Example {

    @Id
    @GeneratedValue(generator="example_seq")
    @SequenceGenerator(name="example_seq",sequenceName="EXAMPLE_SEQ", allocationSize=1)
    private Integer id;

    @Column(nullable = false)
    private String example;

    @ManyToOne
    @JoinColumn(name = "WORD_ID", nullable = false)
    private Word word;

}
