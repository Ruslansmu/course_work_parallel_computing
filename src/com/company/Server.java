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
        } catch (IOException ignored) {}
    }

    public static void sendAnswer(HashSet<String> answer){
        send(Integer.toString(answer.size()));
        for (String num : answer) {
            send(num);
        }
    }

    public static void main(String[] args) throws InterruptedException {


        try {
            try  {
                server = new ServerSocket(4004); // серверсокет прослушивает порт 4004
                System.out.println("Сервер запущен!"); // хорошо бы серверу
                System.out.println("проводиться индексация"); // хорошо бы серверу
                Indexer indexer = new Indexer();
                System.out.println("Индексация завершена!");
                //   объявить о своем запуске
                clientSocket = server.accept(); // accept() будет ждать пока
                //кто-нибудь не захочет подключиться
                try { // установив связь и воссоздав сокет для общения с клиентом можно перейти
                    // к созданию потоков ввода/вывода.
                    // теперь мы можем принимать сообщения
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    // и отправлять
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    while(true) {
                        String request = in.readLine(); // ждём пока клиент что-нибудь нам напишет
                        System.out.println(request);
                        HashSet<String> answer = indexer.find(request);
                        // не долго думая отвечает клиенту
                        sendAnswer(answer);
                    }


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
