package org.example.client;

import org.example.server.ServerMain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.println("Введите ваш логин: ");
        String login = console.nextLine();
        ObjectInputStream in;
        ObjectOutputStream out;
        Map<String, String> msgTo;

        try {
            Socket server = new Socket("localhost", ServerMain.SERVER_PORT);
            System.out.println("Подключение к серверу успешно " + server);
            in = new ObjectInputStream(server.getInputStream());
            out = new ObjectOutputStream(server.getOutputStream());
            msgTo = new HashMap<>();
            msgTo.put("login", login);
            out.writeObject(msgTo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Boolean work = true;


        // Поток на чтение
        new Thread(() -> {
            Boolean workRead = work;
            while (workRead) {
                Map<String, String> msgFrom;
                try {
                    msgFrom = (Map<String, String>) in.readObject();
                    if (Objects.equals("exit", msgFrom.get("new"))) {
                        workRead = false;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Новое сообщение: " + msgFrom.get("new"));

                // TODO: exit пока не реализовываем
            }
        }).start();

        // Поток на запись
        new Thread(() -> {
            Boolean workWrite = work;
            while (workWrite) {
                String inputFromConsole = console.nextLine();
                if (Objects.equals("exit", inputFromConsole)) {
                    workWrite = false;
                }
                Map<String, String> newMsg = new HashMap<>();
                newMsg.put("new", inputFromConsole);
                try {
                    out.writeObject(newMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

}



