package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server {
    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет


    private static void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
    }

    public static void sendAnswer(HashSet<String> answer) {//Отправление ответа на клиент
        send(Integer.toString(answer.size()));
        for (String num : answer) {
            send(num);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            try {
                server = new ServerSocket(4004);
                System.out.println("Сервер запущен!");
                System.out.println("Проводиться индексация");
                Indexer indexer = new Indexer();//при обьявлению обьекта проводим индексацию
                System.out.println("Индексация завершена!");
                clientSocket = server.accept(); // ждем подключение клиента
                try {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    while (true) {
                        String request = in.readLine(); //ожидаем ответа от клиента
                        System.out.println(request);
                        HashSet<String> answer = indexer.find(request);//проводиться поиск запроса
                        sendAnswer(answer);//ответ клиенту
                    }

                } finally { // закрытие сервера
                    clientSocket.close();
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
