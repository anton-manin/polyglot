package org.projects.polyglot.core.service;

import org.projects.polyglot.core.domain.Example;
import org.projects.polyglot.core.domain.Property;
import org.projects.polyglot.core.domain.Word;
import org.projects.polyglot.core.errorhandling.UniqueWordException;
import org.projects.polyglot.core.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static org.projects.polyglot.core.util.Util.emptyIfNull;
import static org.springframework.data.domain.Example.of;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
public class WordService {

    @Autowired
    WordRepository wordRepository;

    public Word save(Word word) {
        if (!wordRepository.findOne(
                of(Word.builder().word(word.getWord()).build(),
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

    public Word update(Word word) {
        if (word.getId() == null) {
            throw new RuntimeException("Word doesnt exist in Database");
        }

        wordRepository
                .findById(word.getId())
                .orElseThrow(() -> new RuntimeException("Word doesnt exist in Database"));

        return wordRepository.saveAndFlush(word);
    }

    public void addTranslation(Word word, Word translation) {
        if (word.getTranslations() == null) {
            word.setTranslations(new ArrayList<>());
        }

        Word storedTranslation = wordRepository.findOne(
                of(Word.builder().word(translation.getWord()).build(),
                        ExampleMatcher.matching().withMatcher("word", exact()))).orElse(null);
//            translation.setTranslations(new ArrayList<>(Arrays.asList(word)));
//            save(translation);
//        }
        if (storedTranslation == null) {
            save(translation);
        } else {
            translation = storedTranslation;
        }

        if (translation.getTranslations() == null) {
            translation.setTranslations(new ArrayList<>());
        }

        translation.getTranslations().add(word);
        word.getTranslations().add(translation);
        update(word);
        update(translation);
    }

    public void deleteTranslation(Word word, Word translation) {
        if (translation.getId() != null) {
            translation.getTranslations().remove(word);
            update(translation);

            word.getTranslations().remove(translation);
            update(word);
        }
    }

    public Optional<Word> find(String wordToSearch) {
        return wordRepository.findOne(
                of(Word.builder().word(wordToSearch).build(),
                        ExampleMatcher.matching().withMatcher("word", exact())));
    }

    public Word addExample(Word word, Example example) {
        if (word.getExamples() == null) {
            word.setExamples(new ArrayList<>());
        }

        word.getExamples().add(example);
        return update(word);
    }

    public Word deleteExample(Word word, Example example) {
        if (word.getExamples() != null) {
            word.getExamples().remove(example);
            example.setWord(null);
        }

        return update(word);
    }

    public Word addProperty(Word word, Property property) {
        if (word.getProperties() == null) {
            word.setProperties(new ArrayList<>());
        }

        word.getProperties().add(property);
        return update(word);
    }

    public Word deleteProperty(Word word, Property property) {
        if (word.getProperties() != null) {
            word.getProperties().remove(property);
            property.setWord(null);
        }

        return update(word);
    }

}
