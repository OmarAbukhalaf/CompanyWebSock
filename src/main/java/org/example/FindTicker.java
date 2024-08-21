package org.example;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.*;

import static org.example.LiveData.subsMap;

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

    public static boolean validateUID(String message){
        String uid=message.substring(4);
        return uid.length() == 9 && isNumber(uid);
    }

    private static boolean isNumber(String num){
        if(num.isEmpty()) return false;

        for(char n: num.toCharArray()){
            if(!Character.isDigit(n))
                return false;
        }
        return true;
    }

    public static List<Session> getSessions(String ticker){
        List<Session> result = new ArrayList<>();
        for(Map.Entry<Session,String> entry: subsMap.entrySet()){
            if (entry.getValue().equals(ticker))
                result.add(entry.getKey());
        }
        return result;
    }
}


