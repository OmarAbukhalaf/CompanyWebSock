package org.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class JettyServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8000);

        WebSocketHandler handler = new WebSocketHandler() {
            public void configure(WebSocketServletFactory factory) {
                factory.register(LiveData.class);
            }
        };

        server.setHandler(handler);
        server.start();
        System.out.println("Server Started");
        server.join();
    }
}


