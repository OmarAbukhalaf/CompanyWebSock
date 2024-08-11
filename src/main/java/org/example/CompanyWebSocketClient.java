package org.example;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class CompanyWebSocketClient extends WebSocketClient {
    private List<String> tickers;
    private Map<String,List<Integer>> volumeMap;
    private WritetoFile writetofile;
    private MessageProcessor messageProcessor;

    public CompanyWebSocketClient(URI link, List<String> tickers) {
        super(link);
        this.tickers = tickers;
        this.volumeMap=new HashMap<>();
        this.writetofile=new WritetoFile();
        this.messageProcessor=new MessageProcessor(volumeMap,writetofile);
        for(String ticker:tickers) {
            volumeMap.put(ticker, new ArrayList<>());
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected");
        send("uid=124667865");
        for(String ticker:tickers) {
            send("subscribe=QO." + ticker + ".ADX");
        }
    }

    @Override
    public void onMessage(String message) {
        String ticker=messageProcessor.getTicker(message);
        WritetoFile.writefile(ticker,message);
        messageProcessor.processMessage(message,ticker);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

}

