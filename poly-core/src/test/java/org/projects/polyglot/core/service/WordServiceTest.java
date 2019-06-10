package org.projects.polyglot.core.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.projects.polyglot.core.domain.*;
import org.projects.polyglot.core.errorhandling.UniqueWordException;
import org.projects.polyglot.core.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.Assert.*;
import static org.springframework.data.domain.Example.of;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

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
        // Given
        Word word = new Word();
        word.setWord("Test");
        word.setLanguage(Languages.ENGLISH);
        word.setWordType(WordType.NOUN);

        // When
        Word persistedWord = sut.save(word);

        // Then
        assertTrue(wordRepository.exists(of(word)));
        assertTrue(wordRepository.findById(persistedWord.getId()).isPresent());
    }

    @Test(expected = UniqueWordException.class)
    public void saveNewWordWhichAlreadyExists() {
        // Given
        Word word = new Word();
        word.setWord("Test");
        word.setLanguage(Languages.ENGLISH);
        word.setWordType(WordType.NOUN);

        // When
        sut.save(word);
        sut.save(word);
    }

    @Test
    public void saveTwoDifferentWords() {
        // Given
        Word word1 = new Word();
        word1.setWord("Test");
        word1.setLanguage(Languages.ENGLISH);
        word1.setWordType(WordType.NOUN);

        Word word2 = new Word();
        word2.setWord("testen");
        word2.setLanguage(Languages.GERMAN);
        word2.setWordType(WordType.VERB);

        // When
        sut.save(word1);
        sut.save(word2);

        // Then
        assertTrue(wordRepository.exists(of(word1)));
        assertTrue(wordRepository.exists(of(word2)));
    }

    @Test
    public void deleteOneWord() {
        // Given
        Word word = new Word();
        word.setWord("Test");
        word.setLanguage(Languages.ENGLISH);
        word.setWordType(WordType.NOUN);
        word.setPriority(1);

        Example example = new Example();
        example.setWord(word);
        example.setExample("I love testing!");

        Property property = new Property();
        property.setWord(word);
        property.setKey("article");
        property.setValue("the");

        Word translation = new Word();
        translation.setWord("Translation");
        translation.setLanguage(Languages.FRENCH);
        translation.setWordType(WordType.UTILITY);
        translation.setPriority(2);

        word.setTranslations(Arrays.asList(translation));
        translation.setTranslations(new ArrayList<>(Arrays.asList(word)));
        word.setExamples(Arrays.asList(example));
        word.setProperties(Arrays.asList(property));

        wordRepository.saveAll(Arrays.asList(word, translation));
        assertNotNull(wordRepository.findById(word.getId()));

        // When
        sut.delete(word);

        // Then
        assertTrue(wordRepository.findAll(of(word)).isEmpty());
        assertEquals(1, wordRepository.findAll(of(translation)).size());
        assertTrue(wordRepository.findAll(of(translation)).get(0).equals(translation));
    }


}
