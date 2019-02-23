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
public class Property {

    @Id
    @GeneratedValue(generator="property_seq")
    @SequenceGenerator(name="property_seq",sequenceName="PROPERTY_SEQ", allocationSize=1)
    private Integer id;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "WORD_ID", nullable = false)
    private Word word;

}
