package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {

    private List<String> words;
    private Set<String> wordSet;
    private Random random;


    public WordleDictionary(List<String> rawWords) {
        if (rawWords == null) {
            throw new IllegalArgumentException("Список слов не может быть null!");
        }

        this.words = new ArrayList<>();
        this.wordSet = new HashSet<>();
        this.random = new Random();

        for (String rawWord : rawWords) {
            String normalized = normalize(rawWord);

            if (isValidWord(normalized)) {
                if (!wordSet.contains(normalized)) {
                    this.words.add(normalized);
                    this.wordSet.add(normalized);
                }
            }
        }

        if (words.isEmpty()) {
            throw new DictionaryLoadException("Словарь пуст! Нет подходящих слов для игры.");
        }
    }

    public static String normalize(String word) {
        if (word == null) {
            return null;
        }

        return word.trim().toLowerCase(Locale.forLanguageTag("ru")).replace('ё', 'е');
    }

    public static boolean isValidWord(String word) {
        if (word == null || word.length() != 5) {
            return false;
        }

        for (char c : word.toCharArray()) {
            if (!isRussianLetter(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isRussianLetter(char c) {
        return (c >= 'а' && c <= 'я') ||
                (c >= 'А' && c <= 'Я') ||
                c == 'ё' || c == 'Ё';
    }

    public boolean contains(String word) {
        if (word == null) {
            return false;
        }

        return wordSet.contains(normalize(word));
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            throw new DictionaryLoadException("Словарь пуст!");
        }
        return words.get(random.nextInt(words.size()));
    }

    public List<String> getAllWords() {
        return new ArrayList<>(words);
    }

    public int size() {
        return words.size();
    }
}
