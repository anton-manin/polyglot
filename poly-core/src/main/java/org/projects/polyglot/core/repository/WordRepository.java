package org.projects.polyglot.core.repository;

import org.projects.polyglot.core.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface WordRepository extends JpaRepository<Word, Integer> {
}
