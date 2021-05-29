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
    public static Map<String,ArrayList<String>> multiMap = new HashMap<String,ArrayList<String>>();
    public static ArrayList<String> emptyArr = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        readFile();
        // server();
        find("from some any");
    }

    public static void readFile(){

        long m = System.currentTimeMillis();
        File dir = new File("/Users/ruslanl/Documents/6semester/Паралельки/course/testSet");

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++){
            System.out.println(files[i]);
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

    public static String indexBuild(String word, File file){
        System.out.println(file.getName());
        String[] words = word.split(" ");
        for(String wordIn : words){
            System.out.println(wordIn);
            String docID = file.getParent()+ "/" +file.getName();
            System.out.println(docID);

            if (multiMap.containsKey(wordIn)){
                if (multiMap.get(wordIn).contains(docID)){
                    continue;
                }
                multiMap.get(wordIn).add(docID);
            }
            else {
                multiMap.put(wordIn, new ArrayList<>());
                multiMap.get(wordIn).add(docID);
            }
            System.out.println("test");
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

    public static void find(String phrase){
        String[] words = phrase.split("\\W+");
        HashSet<String> res = new HashSet<String>(multiMap.get(words[0]));

        for(String word: words){
            res.retainAll(multiMap.get(word));
        }
      //  System.out.println(res);

        if(res.size()==0) {
            System.out.println("Not found");
            return;
        }
        System.out.println("Found in: ");
        for(String num : res){
            System.out.println("\t"+ num);
        }
    }

}
