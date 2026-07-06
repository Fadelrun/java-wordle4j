package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class WordleTest {

    @Test
    void testNormalize() {
        assertEquals("гонец", WordleDictionary.normalize("ГОНЕЦ"));
        assertEquals("ежик", WordleDictionary.normalize("Ёжик"));
        assertEquals("привет", WordleDictionary.normalize("ПриВет"));
    }

    @Test
    void testIsValidLength() {
        assertTrue(WordleDictionary.isValidWord("гонец"));
        assertTrue(WordleDictionary.isValidWord("слово"));
        assertFalse(WordleDictionary.isValidWord("привет"));
        assertFalse(WordleDictionary.isValidWord("кот"));
        assertFalse(WordleDictionary.isValidWord(null));
    }

    @Test
    void testIsValidWord() {
        assertTrue(WordleDictionary.isValidWord("гонец"));
        assertTrue(WordleDictionary.isValidWord("слово"));

        assertFalse(WordleDictionary.isValidWord("привет")); // 6 букв
        assertFalse(WordleDictionary.isValidWord("кот"));    // 3 буквы

        assertFalse(WordleDictionary.isValidWord("abcde"));
        assertFalse(WordleDictionary.isValidWord("12345"));

        assertFalse(WordleDictionary.isValidWord(null));
    }

    @Test
    void testDictionaryContains() {
        List<String> words = Arrays.asList("гонец", "герой", "слово");
        WordleDictionary dictionary = new WordleDictionary(words);

        assertTrue(dictionary.contains("гонец"));
        assertTrue(dictionary.contains("ГОНЕЦ"));
        assertFalse(dictionary.contains("несуществующее"));
    }

    @Test
    void testGetRandomWord() {
        List<String> words = Arrays.asList("гонец", "герой", "слово");
        WordleDictionary dictionary = new WordleDictionary(words);

        String randomWord = dictionary.getRandomWord();
        assertNotNull(randomWord);
        assertTrue(words.contains(randomWord));
    }

    @Test
    void testHintExactMatch() {
        WordleDictionary dictionary = new WordleDictionary(Arrays.asList("герой"));
        WordleGame game = new WordleGame(dictionary, "герой");

        String hint = game.makeTurn("герой");
        assertEquals("+++++", hint);
        assertTrue(game.isWin());
    }

    @Test
    void testHintAllWrong() {
        WordleDictionary dictionary = new WordleDictionary(Arrays.asList("герой", "абвуд"));
        WordleGame game = new WordleGame(dictionary, "герой");

        String hint = game.makeTurn("абвуд");
        assertEquals("-----", hint);
        assertFalse(game.isWin());
    }

    @Test
    void testHintMixed() {
        WordleDictionary dictionary = new WordleDictionary(Arrays.asList("герой", "гонец"));
        WordleGame game = new WordleGame(dictionary, "герой");
        String hint = game.makeTurn("гонец");
        assertEquals("+^-^-", hint);
    }

    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        List<String> words = Arrays.asList("герой", "гонец", "слово", "кот", "город");
        dictionary = new WordleDictionary(words);
        game = new WordleGame(dictionary, "герой");
    }

    @Test
    void testInitialState() {
        assertEquals(6, game.getRemainingAttempts());
        assertFalse(game.isGameOver());
        assertFalse(game.isWin());
        assertEquals("герой", game.getSecretWord());
    }

    @Test
    void testWin() {
        String hint = game.makeTurn("герой");
        assertEquals("+++++", hint);
        assertTrue(game.isGameOver());
        assertTrue(game.isWin());
    }

    @Test
    void testLose() {
        for (int i = 0; i < 6; i++) {
            game.makeTurn("гонец");
        }

        assertTrue(game.isGameOver());
        assertFalse(game.isWin());
        assertEquals(0, game.getRemainingAttempts());
    }

    @Test
    void testWordNotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            game.makeTurn("макра");
        });
        assertEquals(6, game.getRemainingAttempts());
    }

    @Test
    void testGameOverCantMove() {
        game.makeTurn("герой");
        assertThrows(IllegalStateException.class, () -> {
            game.makeTurn("гонец");
        });
    }

}
