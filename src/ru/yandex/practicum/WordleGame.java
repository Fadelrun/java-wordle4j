package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordleGame {
    private static final int WORD_LENGTH = 5;
    private WordleDictionary dictionary;
    private String secretWord;
    private int remainingAttempts;
    private boolean isGameOver;
    private boolean isWin;

    private List<String> guessesHistory;
    private List<String> hintsHistory;
    private Set<String> usedHints;

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.secretWord = dictionary.getRandomWord();
        this.remainingAttempts = 6;
        this.isGameOver = false;
        this.isWin = false;

        this.guessesHistory = new ArrayList<>();
        this.hintsHistory = new ArrayList<>();
        this.usedHints = new HashSet<>();
    }

    public WordleGame(WordleDictionary dictionary, String secretWord) {
        this.dictionary = dictionary;
        this.secretWord = WordleDictionary.normalize(secretWord);
        this.remainingAttempts = 6;
        this.isGameOver = false;
        this.isWin = false;

        this.guessesHistory = new ArrayList<>();
        this.hintsHistory = new ArrayList<>();
        this.usedHints = new HashSet<>();
    }

    public String makeTurn(String guess) {
        if (isGameOver) {
            throw new IllegalStateException("Игра уже завершена!");
        }

        String normalizedGuess = WordleDictionary.normalize(guess);

        if (!WordleDictionary.isValidWord(normalizedGuess)) {
            throw new InvalidWordLengthException("Слово должно состоять из 5 русских букв!");
        }

        if (!dictionary.contains(normalizedGuess)) {
            throw new WordNotFoundException("Слово не найдено в словаре!");
        }

        remainingAttempts--;

        String hint = buildHint(normalizedGuess, secretWord);

        guessesHistory.add(normalizedGuess);
        hintsHistory.add(hint);

        if (normalizedGuess.equals(secretWord)) {
            isWin = true;
            isGameOver = true;
        } else if (remainingAttempts == 0) {
            isGameOver = true;
            isWin = false;
        }

        return hint;
    }

    private String buildHint(String guess, String answer) {
        char[] result = new char[WORD_LENGTH];
        boolean[] userInAnswer = new boolean[WORD_LENGTH];

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                result[i] = '+';
                userInAnswer[i] = true;
            } else {
                result[i] = ' ';
            }
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            if (result[i] == ' ') {
                char c = guess.charAt(i);
                int pos = findCharInAnswer(c, answer, userInAnswer);
                if (pos >= 0) {
                    result[i] = '^';
                    userInAnswer[pos] = true;
                } else {
                    result[i] = '-';
                }
            }
        }

        return new String(result);
    }

    public String getWordHint() {
        if (isGameOver) {
            throw new IllegalStateException("Игра уже завершена!");
        }

        for (String candidate : dictionary.getAllWords()) {
            if (guessesHistory.contains(candidate)) {
                continue;
            }

            if (usedHints.contains(candidate)) {
                continue;
            }

            if (!isCandidateMatchesHistory(candidate)) {
                continue;
            }

            usedHints.add(candidate);
            return candidate;
        }

        throw new IllegalStateException("Нет подходящих слов для подсказки!");
    }

    private boolean isCandidateMatchesHistory(String candidate) {
        for (int i = 0; i < guessesHistory.size(); i++) {
            String hint = buildHint(guessesHistory.get(i), candidate);
            if (!hint.equals(hintsHistory.get(i))) {
                return false;
            }
        }

        return true;
    }

    private int findCharInAnswer(char c, String answer, boolean[] used) {
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == c && !used[i]) {
                return i;
            }
        }

        return -1;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isWin() {
        return isWin;
    }

    public String getSecretWord() {
        return secretWord;
    }
}
