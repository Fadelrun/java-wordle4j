package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class WordleDictionaryLoader {
    public static WordleDictionary load(String fileName){
        List<String> rawWords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                rawWords.add(line);
            }
        } catch (IOException e) {
            throw new DictionaryLoadException("Не удалось загрузить словарь: " + fileName, e);
        }

        if (rawWords.isEmpty()) {
            throw new DictionaryLoadException("Словарь пуст или не содержит подходящих слов!");
        }

        return new WordleDictionary(rawWords);
    }
}
