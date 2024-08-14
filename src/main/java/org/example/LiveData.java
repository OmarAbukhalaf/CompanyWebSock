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

@WebSocket
public  class LiveData {
    public static List<Session> sessions = new ArrayList<>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket connected " );
        sessions.add(session);
       sendMessage("Connected to Jetty server");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Received message:" + message);
        sendMessage(message);
    }
    public static void outputmsg(String message){
        sendMessage(message);
    }
    public static Session getSession(){
        return sessions.getFirst();
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("WebSocket closed");
        sessions.remove(session);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.out.println("WebSocket error" );
    }

    public static synchronized void sendMessage(String message) {
        System.out.println("Sent" + sessions.size() +": "+message);
        for (Session session : sessions) {
            try {
                session.getRemote().sendString(message);
            } catch (IOException e) {
                System.out.println("Error: "+ e);
            }

        }
    }

}
