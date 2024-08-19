package org.example;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.FindTicker.getTicker;
import static org.example.FindTicker.validateUID;


@WebSocket
public  class LiveData {
    private static List<Session> sessions = new ArrayList<>();
    public static String subscribe;
    public static boolean Valid;

    @OnWebSocketConnect
    public synchronized void onConnect(Session session) {
        System.out.println("WebSocket connected " );
        sessions.add(session);
        sendMessage("Connected to Jetty server");
    }

    @OnWebSocketMessage
    public synchronized void onMessage(Session session, String message) {
        System.out.println("Received message:" + message);
        if(message.startsWith("uid="))
            Valid=validateUID(message);

        if(message.startsWith("subscribe"))
            subscribe=getTicker(message);
    }


    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("WebSocket closed");
        sessions.remove(session);

    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.out.println("WebSocket error" + throwable );
    }

    public static synchronized void sendMessage(String message) {
            System.out.println("Sent " + sessions.size() + ": " + message);
            for (Session session : sessions) {
                try {
                    session.getRemote().sendString(message);
                } catch (IOException e) {
                    System.out.println("Error: " + e);
                }
            }
    }
}
