package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
      readFile();
    }

    public static void readFile(){
        long m = System.currentTimeMillis();
        File dir = new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet");
        Map<String,ArrayList<String>> multiMap = new HashMap<String,ArrayList<String>>();
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++){
            //System.out.println(files[i]);

            try (BufferedReader br = new BufferedReader(new FileReader(files[i]))) {
                //чтение построчно
                String s;
                while ((s = br.readLine()) != null) {
                    s = refactText(s);
                    System.out.println(s);
                    tokenBuild(s, multiMap, files[i]);

                }
            } catch (IOException ex) {

                System.out.println(ex.getMessage());
            }
        }
        System.out.println("debug");
        System.out.println(System.currentTimeMillis() - m);
    }

    public static String tokenBuild(String word, Map<String,ArrayList<String>> multiMap, File file){
        ArrayList<String> listFile = new ArrayList<>();
        String[] words = word.split(" ");
        for(String wordIn : words){
            System.out.println(wordIn);
            String docID = file.getParent()+file.getName();
            if (multiMap.containsKey(wordIn)){
                multiMap.get(wordIn).add(docID);
            }
            else {
                multiMap.put(wordIn, listFile);
                multiMap.get(wordIn).add(docID);
            }
        }

        return word;
    }

    public static String refactText(String text){
        text = text.toLowerCase();
        text = text.replaceAll("<br /><br />", " ");
        text = text.replaceAll("[^A-Za-zА-Яа-я0-9]", " ");
        text = text.replaceAll("\\s+", " ");
        return text;
    }
}
