package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


class Thread extends java.lang.Thread {
    public Map<String, ConcurrentLinkedQueue<String>> multiMap;
    public ArrayList<String> stopWords;
    ArrayList<File> allFilesList;
    int startIndex;
    int endIndex;

    public Thread(ArrayList<File> allFilesArr, int startIndex, int endIndex, ArrayList<String> stopWords, Map<String, ConcurrentLinkedQueue<String>> multiMap) { //конструктор класу, приймає дані для обчислень
        this.allFilesList = allFilesArr;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.stopWords = stopWords;
        this.multiMap = multiMap;
    }

    @Override
    public void run() { //выполнения операции в потоке
        for (int j = startIndex; j < endIndex; j++) {
            fileOpen(allFilesList.get(j));
        }
    }

    public void fileOpen(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            //чтение построчно
            String wordKey;
            while ((wordKey = br.readLine()) != null) {
                wordKey = refactText(wordKey);
                indexBuild(wordKey, file);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //построение индекса
    public void indexBuild(String line, File file) {
        String[] words = line.split(" ");
        for (String word : words) {
            String docID = file.getParent() + "/" + file.getName();
            if (stopWords.contains(word)) {
                continue;
            }
            if (!multiMap.containsKey(word)) {
                multiMap.put(word, new ConcurrentLinkedQueue<>());
                multiMap.get(word).add(docID);
            } else {
                if (!multiMap.get(word).contains(docID)) {
                    multiMap.get(word).add(docID);
                }
            }
        }
    }

    public String refactText(String text) {
        text = text.toLowerCase();
        text = text.replaceAll("<br /><br />", " ");
        text = text.replaceAll("[^A-Za-zА-Яа-я0-9]", " ");
        text = text.replaceAll("\\s+", " ");
        return text;
    }
}

