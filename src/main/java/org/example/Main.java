package org.example;
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

    public static void main(String[] args) throws IOException, URISyntaxException {
        String serverURL = "http://tickerchart.com/m/server-apis/companies-with-tags/list/marketId/5";
        List<String> tickers = extractTickers(serverURL);
        URI link= new URI("wss://eu-adx.live.tickerchart.net/streamhubws/");
        CompanyWebSocketClient client = new CompanyWebSocketClient(link, tickers);
        //client.connect();
    }

    private static List<String> extractTickers(String serverURL) throws IOException {
        List<String> tickers = new ArrayList<>();
        URL url = new URL(serverURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while((line=input.readLine())!=null){
            response.append(line);
        }
        input.close();
        conn.disconnect();
        String companiesList=response.toString();
        JSONArray resp = new JSONArray(companiesList);
        for (int i=0;i<resp.length();i++) {
            JSONObject comp = resp.getJSONObject(i);
            tickers.add(comp.getString("TICKER"));
        }

        return tickers;
    }
}



