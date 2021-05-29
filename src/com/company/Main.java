package com.company;

import java.io.*;
import java.lang.reflect.AnnotatedArrayType;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.company.Server.server;

public class Main {
    public static Map<String, ArrayList<String>> multiMap = new HashMap<String, ArrayList<String>>();
    public static ArrayList<String> emptyArr = new ArrayList<>();
    public static ArrayList<String> stopWords = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        initStopWords();
        for (String stops : stopWords) {
            System.out.println(stops);
        }
        readFile();

        // server();
        find("i");

    }


    public static void readFile() {

        long m = System.currentTimeMillis();
        File dir = new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet");

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            //System.out.println(files[i]);
            try (BufferedReader br = new BufferedReader(new FileReader(files[i]))) {
                //чтение построчно
                String s;
                //System.out.println(files[i]);
                while ((s = br.readLine()) != null) {
                    s = refactText(s);
                    // System.out.println(s);

                    indexBuild(s, files[i]);

                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("debug");
        System.out.println(System.currentTimeMillis() - m);
    }

    public static String indexBuild(String word, File file) {
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

    public static String refactText(String text) {
        text = text.toLowerCase();
        text = text.replaceAll("<br /><br />", " ");
        text = text.replaceAll("[^A-Za-zА-Яа-я0-9]", " ");
        text = text.replaceAll("\\s+", " ");
        return text;
    }

    public static void find(String phrase) {
        phrase = phrase.toLowerCase();
        phrase = phrase.replaceAll("[^A-Za-zА-Яа-я0-9]", " ");
        String[] words = phrase.split("\\W+");
        if(!multiMap.containsKey(words)){
            System.out.println("No result");
        } else {
            HashSet<String> res = new HashSet<String>(multiMap.get(words[0]));


            for (String word : words) {
                if (stopWords.contains(word)) {
                    continue;
                }
                res.retainAll(multiMap.get(word));
            }

            if (res.size() == 0) {
                System.out.println("Not found");
                return;
            }
            if (res == null) {
                System.out.println("Not found");
                return;
            }
            System.out.println("Found in: ");
            for (String num : res) {
                System.out.println("\t" + num);
            }
        }
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
