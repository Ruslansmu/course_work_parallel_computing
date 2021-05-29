package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


class Thread1 extends Thread {//конструктор
    public Map<String, ArrayList<String>> multiMap = new HashMap<String, ArrayList<String>>();
    public ArrayList<String> stopWords = new ArrayList<String>();
    ArrayList<File> allFilesArr = new ArrayList<>();
    int startIndex;
    int endIndex;
    public Thread1(ArrayList<File> allFilesArr, int startIndex, int endIndex, ArrayList<String> stopWords, Map<String, ArrayList<String>> multiMap) { //конструктор класу, приймає дані для обчислень
        this.allFilesArr = allFilesArr;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.stopWords = stopWords;
        this.multiMap = multiMap;
    }

    @Override
    public void run(){ //обрахунки, що здійснюватимуться в зазначеному потоці

        for(int j = startIndex; j<endIndex; j++ ){
            fileOpen(allFilesArr.get(j));
        }
    }

    public void fileOpen(File file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            //чтение построчно
            String s;
            while ((s = br.readLine()) != null) {
                s = refactText(s);
                indexBuild(s, file);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String indexBuild(String word, File file) {
        String[] words = word.split(" ");
        for (String wordIn : words) {
            String docID = file.getParent() + "/" + file.getName();

            if (stopWords.contains(wordIn)) {
                continue;
            }
            if (multiMap.containsKey(wordIn)) {
                if (multiMap.get(wordIn).contains(docID)) {
                    continue;
                }
                multiMap.get(wordIn).add(docID);
            } else {
                multiMap.put(wordIn, new ArrayList<>());
                multiMap.get(wordIn).add(docID);
            }
        }

        return word;
    }

    public String refactText(String text) {
        text = text.toLowerCase();
        text = text.replaceAll("<br /><br />", " ");
        text = text.replaceAll("[^A-Za-zА-Яа-я0-9]", " ");
        text = text.replaceAll("\\s+", " ");
        return text;
    }


}

