package org.projects.polyglot.core.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.projects.polyglot.core.domain.Languages;
import org.projects.polyglot.core.domain.Word;
import org.projects.polyglot.core.domain.WordType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class WordRepositoryIntegrationTest {

    @Autowired
    WordRepository wordRepository;

    @Test
    public void findAll() {
        System.out.println("################################################");
        System.out.println("Words in the database: " + wordRepository.count());
        System.out.println("################################################");
    }

    @Test
    public void test() {
        Word word = Word.builder()
                .language(Languages.ENGLISH)
                .word("Test")
                .wordType(WordType.PHRASE)
                .priority(1)
                .build();

        wordRepository.save(word);
        Optional<Word> result = wordRepository.findById(word.getId());
        assertTrue(result.isPresent());
    }
}
