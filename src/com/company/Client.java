package com.company;
import java.io.*;
import java.net.Socket;
import java.util.HashSet;

public class Client {
    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader; // нам нужен ридер читающий с консоли, иначе как
    // мы узнаем что хочет сказать клиент?
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    private static void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }

    public static void exeptedAnswer() throws IOException {
        int n = Integer.parseInt(in.readLine());

        for (int i = 0; i < n; i++) {
            System.out.println(in.readLine());
        }
    }

    public static void main(String[] args) {
        try {
            try {
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем
                //  у сервера доступ на соединение
                reader = new BufferedReader(new InputStreamReader(System.in));
                // читать соообщения с сервера
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // писать туда же
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                while(true) {
                    System.out.println("Вы что-то хотели сказать? Введите это здесь:");
                    // если соединение произошло и потоки успешно созданы - мы можем
                    //  работать дальше и предложить клиенту что то ввести
                    // если нет - вылетит исключение
                    String request = reader.readLine(); // ждём пока клиент что-нибудь
                    while (request.isEmpty()){
                        System.out.println("write some word");
                        request = reader.readLine(); // ждём пока клиент что-нибудь
                    }
                    // не напишет в консоль
                    send(request);
                    exeptedAnswer();
                }
            } finally { // в любом случае необходимо закрыть сокет и потоки
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
