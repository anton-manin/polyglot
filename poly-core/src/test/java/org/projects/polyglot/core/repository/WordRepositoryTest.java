package org.projects.polyglot.core.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.projects.polyglot.core.domain.Example;
import org.projects.polyglot.core.domain.Languages;
import org.projects.polyglot.core.domain.Property;
import org.projects.polyglot.core.domain.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WordRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    WordRepository wordRepository;

    @Test
    public void noWords() {
        List<Word> words = wordRepository.findAll();

        assertNotNull(words);
        assertEquals(0, words.size());
    }

    @Test
    public void saveNewWordMinimal() {
        Languages language = Languages.ENGLISH;
        int priority = 1;
        String wordText = "Test";

        Word word = Word.builder()
                .language(language)
                .priority(priority)
                .word(wordText)
                .build();

        wordRepository.save(word);
        List<Word> words = wordRepository.findAll();

        assertNotNull(words);
        assertEquals(1, words.size());
        Word resultWord = words.get(0);
        assertNotNull(resultWord.getId());
        assertEquals(language, resultWord.getLanguage());
        assertEquals(priority, resultWord.getPriority().intValue());
        assertEquals(wordText, resultWord.getWord());
    }

    @Test
    public void saveNewWordWithTranslations() {
        // Given
        Languages languageEnglish = Languages.ENGLISH;
        String wordTextEnglish = "Test in English";
        Word word = Word.builder()
                .word(wordTextEnglish)
                .language(languageEnglish)
                .build();

        Languages languageGerman = Languages.GERMAN;
        String wordTextGerman = "Test in German";
        Word translationGerman = Word.builder()
                .word(wordTextGerman)
                .language(languageGerman)
                .build();
        wordRepository.save(translationGerman);

        Languages languageRussian = Languages.RUSSIAN;
        String wordTextRussian = "Test in Russian";
        Word translationRussian = Word.builder()
                .word(wordTextRussian)
                .language(languageRussian)
                .build();
        wordRepository.save(translationRussian);

        word.setTranslations(Arrays.asList(translationGerman, translationRussian));

        // When
        wordRepository.save(word);
        List<Word> words = wordRepository.findAll();

        // Then
        assertNotNull(words);
        assertEquals(3, words.size());
        // Third word is the english word, because persisted at latest
        Word resultWord = words.get(2);
        assertNotNull(resultWord.getTranslations());
        assertEquals(2, resultWord.getTranslations().size());
        assertEquals(Languages.GERMAN, resultWord.getTranslations().get(0).getLanguage());
        assertEquals(Languages.RUSSIAN, resultWord.getTranslations().get(1).getLanguage());
    }

    @Test
    public void saveNewWordWithExamples() {
        Word word = Word.builder().word("").language(Languages.ENGLISH).build();

        Example example1 = new Example();
        example1.setExample("Example 1");
        example1.setWord(word);

        Example example2 = new Example();
        example2.setExample("Example 2");
        example2.setWord(word);

        word.setExamples(Arrays.asList(example1, example2));

        wordRepository.save(word);
        Optional<Word> resultWord = wordRepository.findById(word.getId());
        assertTrue(resultWord.isPresent());
        assertNotNull(resultWord.get().getExamples());
        assertEquals(2, resultWord.get().getExamples().size());
    }

    @Test
    public void saveNewWordWithProperties() {
        Word word = Word.builder().word("").language(Languages.ENGLISH).build();

        Property property1 = new Property();
        property1.setKey("Key1");
        property1.setValue("Value1");
        property1.setWord(word);

        Property property2 = new Property();
        property2.setKey("Key2");
        property2.setValue("Value2");
        property2.setWord(word);

        word.setProperties(Arrays.asList(property1, property2));

        wordRepository.save(word);
        Optional<Word> resultWord = wordRepository.findById(word.getId());
        assertTrue(resultWord.isPresent());
        assertNotNull(resultWord.get().getProperties());
        assertEquals(2, resultWord.get().getProperties().size());
    }

}
