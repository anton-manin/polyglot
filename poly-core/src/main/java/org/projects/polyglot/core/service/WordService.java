package org.projects.polyglot.core.service;

import org.projects.polyglot.core.domain.Word;
import org.projects.polyglot.core.errorhandling.UniqueWordException;
import org.projects.polyglot.core.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import static org.projects.polyglot.core.util.Util.emptyIfNull;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
public class WordService {

    @Autowired
    WordRepository wordRepository;

    public Word save(Word word) {
        if (!wordRepository.findOne(
                Example.of(Word.builder().word(word.getWord()).build(),
                ExampleMatcher.matching().withMatcher("word", exact()))
                ).isPresent()
        ) {
            return wordRepository.saveAndFlush(word);
        }

        throw new UniqueWordException();
    }

    public void delete(Word word) {
        for (Word translation : emptyIfNull(word.getTranslations())) {
            translation.getTranslations().remove(word);
            wordRepository.saveAndFlush(translation);
        }

        word.setTranslations(null);
        wordRepository.delete(word);
    }
}
