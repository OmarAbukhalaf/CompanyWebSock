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
    private Map<String,Company> company;
    private MessageProcessor messageProcessor;

    public CompanyWebSocketClient(URI link, List<String> tickers) {
        super(link);
        this.tickers = tickers;
        this.company=new HashMap<>();
        this.messageProcessor=new MessageProcessor(company);
        for(String ticker:tickers) {
            company.put(ticker, new Company(ticker));
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

