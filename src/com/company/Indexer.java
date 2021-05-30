package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;



public class Indexer {
    public static Map<String, ConcurrentLinkedQueue<String>> multiMap = new ConcurrentHashMap<>();
    public static ArrayList<String> emptyArr = new ArrayList<>();
    public static ArrayList<String> stopWords = new ArrayList<String>();
    public static ArrayList<File> allFilesArr = new ArrayList<>();
    public static int NUMBER_THREADS = 4;

    public Indexer() throws InterruptedException {//конструктор
        allFiles();
        initStopWords();
        parallels();
    }

    public static void parallels() throws InterruptedException {
        Thread1[] thread = new Thread1[NUMBER_THREADS];

        int sizeOfFilesArray = allFilesArr.size();
        for (int i = 0; i < NUMBER_THREADS; i++) {//разбиваем на потоки
            thread[i] = new Thread1(allFilesArr, sizeOfFilesArray / NUMBER_THREADS * i,
                    i == (NUMBER_THREADS - 1) ? sizeOfFilesArray : sizeOfFilesArray / NUMBER_THREADS * (i + 1), stopWords, multiMap);
            thread[i].start();
        }
        //waiting for finish of threads
        for (int i = 0; i < NUMBER_THREADS; i++) {
            thread[i].join();
        }
    }

    public static void allFiles(){

        File[] dir = {new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet/test/neg"),
                new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet/test/pos"),
                new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet/train/neg"),
                new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet/train/pos"),
                new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet/train/unsup")};
        for (File catalog : dir) {
            File[] files = catalog.listFiles();
            for (int i = 0; i < files.length; i++) {
                allFilesArr.add(files[i]);

            }
        }
    }


    public static HashSet<String> find(String phrase) {

        phrase = phrase.toLowerCase();
        phrase = phrase.replaceAll("[^A-Za-zА-Яа-я0-9]", " ");
        String[] words = phrase.split("\\W+");
        HashSet<String> res = null;

        if(!multiMap.containsKey(words[0]) && words.length == 1){
            res = new HashSet<String>();
            res.add("No result");
            System.out.println("No result");
        } else {
            boolean flag = true;
            for (String word : words) {
                if (stopWords.contains(word)) {
                    continue;
                }
                if(flag){
                    res = new HashSet<String>(multiMap.get(word));//
                    flag = false;
                }
                res.retainAll(multiMap.get(word));
            }
            if (res.size() == 0) {
                res = new HashSet<String>();
                res.add("No result");
                System.out.println("Not found");
            }

            if (res == null) {
                res = new HashSet<String>();
                res.add("No result");
                System.out.println("Not found");
            }
            System.out.println("Found in: ");
            for (String num : res) {
                System.out.println("\t" + num);
            }
        }
        return res;
    }

    public static void initStopWords() {
        File stopWord = new File("/Users/ruslanl/Documents/6semester/Паралельки/course/datasets/aclImdb/stopWords.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(stopWord))) {
            //чтение построчно
            String s;
            while ((s = br.readLine()) != null) {
                stopWords.add(s);
            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

}
