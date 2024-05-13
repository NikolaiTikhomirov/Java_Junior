package org.example.server;

import org.example.client.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final Map<String, ClientConnection> clients = new HashMap<>();
    private boolean work = true;

    public void start(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            while (work) {
                System.out.println("Ждем клиента");
                Socket clientSocket = server.accept();
                System.out.println("Подключился новый клиент: " + clientSocket);
                ClientConnection client = new ClientConnection(clientSocket, this);

                String clientLogin = client.getLogin();

                if (clients.containsKey(clientLogin)) {
                    client.sendMessage("Пользователь с таким логином уже подключен");
                    client.close();
                    continue;
                }

                if (clientLogin.equals("stop")) {
                    work = false;
                    break;
                }


                clients.put(clientLogin, client);
                sendMessageToAll(clientLogin, "Подключился новый клиент: " + clientLogin);

                client.setOnCloseHandler(() -> {
                    clients.remove(clientLogin);
                    sendMessageToAll(clientLogin, "Клиент " + clientLogin + " отключился");
                });

                new Thread(client).start();
            }
        } catch (IOException e) {
            System.err.println("Произошла ошибка во время прослушивания порта " + port + ": " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToClient(String login, String message) {
        ClientConnection clientConnection = clients.get(login);
        clientConnection.sendMessage(message);
    }

    public void sendMessageToAll(String login, String message) {
        for (ClientConnection client : clients.values()) {
            if (!client.getLogin().equals(login)){
                client.sendMessage(message);
            }
        }
    }

}
