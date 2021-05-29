package com.company;

import java.io.*;
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

public class Main {
    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) throws IOException {
        //readFile();
        server();
    }

    public static void readFile(){
        long m = System.currentTimeMillis();
        File dir = new File("/Users/ruslanl/Documents/6semester/Паралельки/course/datasets/aclImdb/train/neg");
        Map<String,ArrayList<String>> multiMap = new HashMap<String,ArrayList<String>>();
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++){
            //System.out.println(files[i]);

            try (BufferedReader br = new BufferedReader(new FileReader(files[i]))) {
                //чтение построчно
                String s;
                while ((s = br.readLine()) != null) {
                    s = refactText(s);
                   // System.out.println(s);
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
            //System.out.println(wordIn);
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

    public static void server() throws IOException {
        
        try {
            try  {
                server = new ServerSocket(4004); // серверсокет прослушивает порт 4004
                System.out.println("Сервер запущен!"); // хорошо бы серверу
                //   объявить о своем запуске
                clientSocket = server.accept(); // accept() будет ждать пока
                //кто-нибудь не захочет подключиться
                try { // установив связь и воссоздав сокет для общения с клиентом можно перейти
                    // к созданию потоков ввода/вывода.
                    // теперь мы можем принимать сообщения
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    // и отправлять
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    String word = in.readLine(); // ждём пока клиент что-нибудь нам напишет
                    System.out.println(word);
                    // не долго думая отвечает клиенту
                    out.write("Привет, это Сервер! Подтверждаю, вы написали : " + word + "\n");
                    out.flush(); // выталкиваем все из буфера

                } finally { // в любом случае сокет будет закрыт
                    clientSocket.close();
                    // потоки тоже хорошо бы закрыть
                    in.close();
                    out.close();
                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}
