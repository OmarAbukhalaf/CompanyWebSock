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
    public static Map<Session,Connection> connectionMap= new HashMap<>();

    @OnWebSocketConnect
    public synchronized void onConnect(Session session) {
        System.out.println("WebSocket connected " );
        Connection connection=new Connection(session,"");
        connectionMap.put(session,connection);
    }

    @OnWebSocketMessage
    public synchronized void onMessage(Session session, String message) {
        System.out.println("Received message:" + message);
        Connection connection=connectionMap.get(session);
        if(message.startsWith("uid=")) {
            String uid = message.substring(4);
            connection.setUid(uid);
        }
        if(message.startsWith("subscribe")) {
            subscribe = getTicker(message);
            connection.addSubscribe(subscribe);
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("WebSocket closed");
        connectionMap.remove(session);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {
        System.out.println("WebSocket error" + throwable );
    }

    public static synchronized void sendMessage(String ticker,String message) {
        System.out.println("Sent : " + message);
        for (Connection connection : connectionMap.values()) {
            if(connection.searchTicker(ticker) && Connection.validateUID(connection.getUid())){
                try {
                    connection.getSession().getRemote().sendString(message);
                } catch (IOException e) {
                    System.out.println("Error sending message: " + e.getMessage());
                }
            }
        }
    }
}


