package org.projects.polyglot.core.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.projects.polyglot.core.domain.Languages;
import org.projects.polyglot.core.domain.Word;
import org.projects.polyglot.core.domain.WordType;
import org.projects.polyglot.core.errorhandling.UniqueWordException;
import org.projects.polyglot.core.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestConfiguration
@ComponentScan(basePackages = {"org.projects.polyglot.core.service", "org.projects.polyglot.core.repository"})
@EntityScan(basePackages = "org.projects.polyglot.core.domain")
public class WordServiceTest {

    @Autowired
    WordService sut;

    @Autowired
    WordRepository wordRepository;

    @After
    public void tearDown() {
        wordRepository.deleteAll();
    }

    @Test
    public void saveNewWord() {
        Word word = new Word();
        word.setWord("Test");
        word.setLanguage(Languages.ENGLISH);
        word.setWordType(WordType.NOUN);

        Word persistedWord = sut.save(word);

        assertTrue(wordRepository.exists(Example.of(word)));
        assertTrue(wordRepository.findById(persistedWord.getId()).isPresent());
    }

    @Test(expected = UniqueWordException.class)
    public void saveNewWordWhichAlreadyExists() {
        Word word = new Word();
        word.setWord("Test");
        word.setLanguage(Languages.ENGLISH);
        word.setWordType(WordType.NOUN);

        sut.save(word);
        sut.save(word);
    }

    @Test
    public void saveTwoDifferentWords() {
        Word word1 = new Word();
        word1.setWord("Test");
        word1.setLanguage(Languages.ENGLISH);
        word1.setWordType(WordType.NOUN);

        Word word2 = new Word();
        word2.setWord("testen");
        word2.setLanguage(Languages.GERMAN);
        word2.setWordType(WordType.VERB);

        sut.save(word1);
        sut.save(word2);

        assertTrue(wordRepository.exists(Example.of(word1)));
        assertTrue(wordRepository.exists(Example.of(word2)));
    }

}
