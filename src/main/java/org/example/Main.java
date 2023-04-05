package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new TreeMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        int countThreads = 1_000;
        int countLetters = 100;
        for (int t = 0; t < countThreads; t++) {
            Runnable procedura = () -> {
                int countRouters = 1_000;
                for (int r = 0; r < countRouters; r++) {
                    String route = generateRoute("RLRFR", countLetters); //Генерируем маршрут из 100 команд
                    int countR = countRInRoute(route, "R".charAt(0)); //Считаем количество команд R в маршруте
                    synchronized (sizeToFreq) {                         // Берем монитор на мапу в текущем потоке и добавляем значения
                        if (sizeToFreq.containsKey(countR)) {
                            sizeToFreq.replace(countR, sizeToFreq.get(countR) + 1);
                        } else {
                            sizeToFreq.putIfAbsent(countR, 1);
                        }
                    }
                }
            };
            Thread thread = new Thread(procedura);
            thread.start(); // Запускаем код в потоке
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join(); // зависаем, ждём когда поток, объект которого лежит в thread завершится
        }
        Integer maxValue = Collections.max(sizeToFreq.values()); // Максимальное значение раз
        Integer maxKey = null; //ключ для максимального значения раз
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getValue() == maxValue) {
                maxKey = entry.getKey();
            }
        }
        System.out.println("Самое частое количество повторений " + maxKey + " (встретилось " + maxValue + " раз)");
        sizeToFreq.remove(maxKey);
        System.out.println("Другие размеры:");

        for (Map.Entry item : sizeToFreq.entrySet()
        ) {
            System.out.println(" - " + item.getKey() + " (" + item.getValue() + " раз)");
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    private static int countRInRoute(String str, char ch) {
        int counter = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                counter++;
            }
        }
        return counter;
    }
}