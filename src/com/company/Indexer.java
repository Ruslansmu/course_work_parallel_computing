package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Indexer {
    public static Map<String, ConcurrentLinkedQueue<String>> multiMap = new ConcurrentHashMap<>();
    public static ArrayList<String> stopWords = new ArrayList<String>();
    public static ArrayList<File> allFilesList = new ArrayList<>();
    public static int THREADS_NUMBER = 16;

    public Indexer() throws InterruptedException {//конструктор
        readAllFilesToList();
        initStopWords();
        parallels();//нахождение индекса с помощью розпаралеливания
    }

    public static void parallels() throws InterruptedException {
        long time = System.currentTimeMillis();
        Thread[] thread = new Thread[THREADS_NUMBER];

        int sizeOfFilesArray = allFilesList.size();
        for (int i = 0; i < THREADS_NUMBER; i++) {//разбиваем на потоки
            thread[i] = new Thread(allFilesList, sizeOfFilesArray / THREADS_NUMBER * i,
                    i == (THREADS_NUMBER - 1) ? sizeOfFilesArray : sizeOfFilesArray / THREADS_NUMBER * (i + 1),
                    stopWords, multiMap);
            thread[i].start();
        }
        // ожидание завершения работы потоков
        for (int i = 0; i < THREADS_NUMBER; i++) {
            thread[i].join();
        }
        System.out.println(System.currentTimeMillis() - time);
    }

    public static void readAllFilesToList() {
        File[] dir = {new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet/test/neg"),
                new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet/test/pos"),
                new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet/train/neg"),
                new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet/train/pos"),
                new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet/train/unsup")};//масив директорий фвйлов
        for (File catalog : dir) {
            File[] files = catalog.listFiles();
            for (int i = 0; i < files.length; i++) {
                allFilesList.add(files[i]);//запись txt файлов в список
            }
        }
    }

    public static HashSet<String> find(String phrase) {
        phrase = phrase.toLowerCase();//перевод в нижний регистр
        phrase = phrase.replaceAll("[^A-Za-zА-Яа-я0-9]", " ");//удаление лишних символов
        String[] words = phrase.split("\\W+");
        HashSet<String> res = null;
        //если нет результата под одному слову
        if (!multiMap.containsKey(words[0]) && words.length == 1) {
            res = new HashSet<String>();
            res.add("Нет результата");
        } else {
            //делаем обьеденение всех docID  по словам
            boolean flag = true;//для обозначения первого элемента
            for (String word : words) {
                if (stopWords.contains(word)) {
                    continue;
                }
                if (flag) {
                    res = new HashSet<String>(multiMap.get(word));//
                    flag = false;
                }
                res.retainAll(multiMap.get(word));
            }
            //проверка результатов на пустой set результатов
            if (res.size() == 0) {
                res = new HashSet<String>();
                res.add("Нет ркзультата");
            }
            if (res == null) {
                res = new HashSet<String>();
                res.add("Нет результата");
            }
        }
        return res;
    }

    public static void initStopWords() {//стоп слова
        //считываем файл со стоп словами
        File stopWord = new File("/Users/ruslanl/Documents/6semester/Паралельки/course/datasets/aclImdb/stopWords.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(stopWord))) {
            //чтение построчно
            String wordStop;
            while ((wordStop = br.readLine()) != null) {
                stopWords.add(wordStop);//записываем стоп слова
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
