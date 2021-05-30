package com.company;

import java.io.*;
import java.net.Socket;


public class Client {
    private static Socket clientSocket; // сокет для общения
    private static BufferedReader reader; // ридер читающий с консоли
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    private static void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
    }

    //получение ответа от сервера
    public static void exeptedAnswer() throws IOException {
        int n = Integer.parseInt(in.readLine());

        for (int i = 0; i < n; i++) {
            System.out.println(in.readLine());
        }
    }

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 4004);
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                while (true) {
                    System.out.println("Напишити слово или словосочетания для поиска:");
                    String request = reader.readLine(); // ждём пока клиент что-нибудь напишет
                    while (request.isEmpty()) {//преверяем пустой ли запрос
                        System.out.println("Введите запрос");
                        request = reader.readLine();
                    }
                    send(request);//отправка запроса на сервер
                    exeptedAnswer();//получение ответа
                }
            } finally { // закрытие клиента
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
