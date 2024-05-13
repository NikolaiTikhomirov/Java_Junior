package org.example.server;

import org.example.client.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Инкапсуляция для клиента на сервере
 */
public class ClientConnection implements Runnable {

    private final Server server;
    private final Socket socket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final String login;
    private Runnable onCloseHandler;
    private Map<String, String> msgFrom;

    public ClientConnection(Socket socket, Server server) throws IOException, ClassNotFoundException {
        this.server = server;
        this.socket = socket;
        this.output = new ObjectOutputStream(this.socket.getOutputStream());
        this.input = new ObjectInputStream(this.socket.getInputStream());

        msgFrom = new HashMap<>();
        msgFrom = (Map<String, String>) input.readObject();
        this.login = msgFrom.get("login");
        System.out.println(login);

    }

    public void sendMessage(String message) {
        try {
            Map<String, String> msgTo = new HashMap<>();
            msgTo.put("new", message);
            output.writeObject(msgTo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLogin() {
        return login;
    }

    public void setOnCloseHandler(Runnable onCloseHandler) {
        this.onCloseHandler = onCloseHandler;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Map<String, String> newMsg;
                newMsg = (Map<String, String>) input.readObject();
                String msgFromClient = newMsg.get("new"); // json

                if (Objects.equals("exit", msgFromClient)) {
                    server.sendMessageToClient(login, msgFromClient);
                    System.out.println("Клиент отключился");
                    break;
                }

                // TODO: распарсить сообщение и понять, что нужно сделать
                if (msgFromClient.startsWith("@")) {
                    // @inchestnov привет!
                    String[] split = msgFromClient.split("\\s+");
                    String loginTo = split[0].substring(1);

                    String pureMessage = msgFromClient.replace("@" + loginTo + " ", "");
                    server.sendMessageToClient(loginTo, "[" + login + "] " + pureMessage);
                } else {
                    server.sendMessageToAll(login, "[" + login + "] " + msgFromClient);
                }
            }

            try {
                close();
            } catch (IOException e) {
                System.err.println("Произошла ошибка во время закрытия сокета: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (onCloseHandler != null) {
                onCloseHandler.run();
            }
        }
    }

    public void close() throws IOException {
        socket.close();
    }

}
