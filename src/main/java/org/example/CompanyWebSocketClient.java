package org.example;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CompanyWebSocketClient extends WebSocketClient {
    private List<String> tickers;
    private Map<String,List<Integer>> volumeMap;

    public CompanyWebSocketClient(URI link, List<String> tickers) {
        super(link);
        this.tickers = tickers;
        this.volumeMap=new HashMap<>();
        for(String ticker:tickers) {
            volumeMap.put(ticker, new ArrayList<>());
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected");
        for(String ticker:tickers) {
            send("uid=124667865");
            send("subscribe=QO." + ticker + ".ADX");
        }
    }

    @Override
    public void onMessage(String message) {
        String ticker=getTicker(message);
        writeToFile(ticker,message);
        processMessage(message,ticker);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
    public void clearFiles() {
        for (String ticker : tickers) {
            try {
                Files.write(Paths.get(ticker + ".txt"), new byte[0]); // Clear the file by writing an empty byte array
                System.out.println("Cleared file for ticker: " + ticker);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeToFile(String ticker, String message) {
        try (FileWriter writer = new FileWriter(ticker + ".txt",true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String message,String ticker) {
        JSONObject m = new JSONObject(message);
        int volume = 0;
        if (m.has("volume") && !m.getString("volume").equals("#")) {
            volume = m.getInt("volume");
        }
        List<Integer> volumes=volumeMap.get(ticker);
        volumes.add(volume);
            if (volumes.size() == 5) {
                int sum=0;
                for(int v:volumes){
                    sum+=v;
                }
                String averageMessage="Average of the last 5 messages is:" + (double)sum/5;
                writeToFile(ticker,averageMessage);
                volumes.clear();
           }
    }
    private String getTicker(String message){
        JSONObject j = new JSONObject(message);
        String topic= j.getString("topic");
        String []parts= topic.split("\\.");
        String ticker=parts[1];
        return ticker;

    }
}
