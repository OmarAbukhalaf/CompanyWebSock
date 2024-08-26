package org.example;
import org.json.JSONObject;

public class FindTicker {
    public static String getTickerJSON(String message){
        JSONObject j = new JSONObject(message);
        String topic= j.getString("topic");
        String []parts= topic.split("\\.");
        String ticker=parts[1];
        return ticker;
    }

    public static String getTicker(String message){
        String[] parts=message.split("\\.");
        return parts[1];
    }


}


