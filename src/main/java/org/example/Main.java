package org.example;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        String serverURL = "";
        String companiesList = extractResponse(serverURL);
        List<String> tickers = extractTickers(companiesList);

        Server server = new Server(8000);
        WebSocketHandler handler = new WebSocketHandler() {
            public void configure(WebSocketServletFactory factory) {
                factory.register(LiveData.class);
            }
        };

        server.setHandler(handler);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server Started");


        URI link;
        try {
            link = new URI("wss://eu-adx.live.tickerchart.net/streamhubws/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        CompanyWebSocketClient client = new CompanyWebSocketClient(link, tickers);
        client.connect();

        try {
            server.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static String extractResponse(String serverURL) throws IOException {
        URL url = new URL(serverURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = input.readLine()) != null) {
            response.append(line);
        }
        input.close();
        conn.disconnect();
        return response.toString();
    }
    private static List <String> extractTickers (String companiesList){
        List<String> tickers = new ArrayList<>();
        JSONArray resp = new JSONArray(companiesList);
        for (int i=0;i<resp.length();i++) {
            JSONObject comp = resp.getJSONObject(i);
            tickers.add(comp.getString("TICKER"));
        }
    return tickers;
    }
}



