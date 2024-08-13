package org.example;

import java.util.ArrayList;
import java.util.List;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

public class LiveData {

    private static List<Session> sessions = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket connection opened: ");
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("WebSocket message received: " + message);
        broadcastMessage("Connected to Jetty server ");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("WebSocket connection closed: ");
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("WebSocket error");
    }

    public static void broadcastMessage(String message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                System.out.println("Error broadcasting message");
            }
        }
    }
}
