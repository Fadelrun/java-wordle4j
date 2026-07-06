package ru.yandex.practicum;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter(new FileWriter("game.log", true))) {

            log.println("Новая игра");

            WordleDictionary dictionary = WordleDictionaryLoader.load("words_ru.txt");
            log.println("Словарь загружен. Количество слов: " + dictionary.size());

            WordleGame game = new WordleGame(dictionary);
            log.println("Загадано слово. Игра начата!");
            log.println("Состояние игры: попыток=" + game.getRemainingAttempts() +
                    ", слово=" + game.getSecretWord());

            Scanner scanner = new Scanner(System.in);

            while (!game.isGameOver()) {
                System.out.println("Осталось попыток: " + game.getRemainingAttempts());
                System.out.print("Введите слово (или Enter для подсказки): ");
                String input = scanner.nextLine();

                try {
                    if (input.trim().isEmpty()) {
                        String hint = game.getWordHint();
                        System.out.println("Подсказка: " + hint);
                        log.println("Подсказка: " + hint);

                        String result = game.makeTurn(hint);
                        System.out.println("Результат: " + result);
                        log.println("Ход по подсказке: " + hint + " -> " + result);
                    } else {
                        String result = game.makeTurn(input);
                        System.out.println(result);
                        log.println("Ход: " + input.trim() + " -> " + result);
                    }

                    if (game.isGameOver()) {
                        if (game.isWin()) {
                            System.out.println("Поздравляем! Вы угадали слово: " + game.getSecretWord());
                            log.println("Победа! Слово: " + game.getSecretWord());
                        } else {
                            System.out.println("Вы проиграли! Загаданное слово: " + game.getSecretWord());
                            log.println("Поражение. Слово: " + game.getSecretWord());
                        }
                    }

                } catch (WordNotFoundException | InvalidWordLengthException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                    log.println("Ошибка ввода: " + e.getMessage());
                } catch (Exception e) {
                    log.println("Критическая ошибка: " + e.getMessage());
                    e.printStackTrace(log);
                    System.out.println("Произошла ошибка. Проверьте лог-файл.");
                    break;
                }
            }

            scanner.close();
            log.println("Игра завершена");

        } catch (Exception e) {
            System.err.println("Ошибка запуска игры: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
