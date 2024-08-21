package org.example;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.*;

import static org.example.FindTicker.*;


@WebSocket
public  class LiveData {

    public static String subscribe;
    public static Map<Session,String> Valid= new HashMap<>();
    public static Map<Session,String> subsMap= new HashMap<>();

    @OnWebSocketConnect
    public synchronized void onConnect(Session session) {
        System.out.println("WebSocket connected " );

    }

    @OnWebSocketMessage
    public synchronized void onMessage(Session session, String message) {
        System.out.println("Received message:" + message);
        if(message.startsWith("uid="))
           if(validateUID(message))
            Valid.put(session,"true");

        if(message.startsWith("subscribe")) {
            subscribe = getTicker(message);
            subsMap.put(session, subscribe);
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("WebSocket closed");
        subsMap.remove(session);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.out.println("WebSocket error" + throwable );
    }

    public static synchronized void sendMessage(String ticker,String message) {
        //System.out.println("Sent " + sessions.size() + ": " + message);
        List<Session> clients = getSessions(ticker);
        for (Session session : clients) {
                try {
                    session.getRemote().sendString(message);
                } catch (IOException e) {
                    System.out.println("Error: " + e);
                }

        }
    }
}


