package org.example;

import org.eclipse.jetty.websocket.api.Session;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Connection {
    private Session session;
    private String uid;
    private Set<String> compSubscribe=new HashSet<>();

    public Connection(Session session,String uid){
        this.session=session;
        this.uid=uid;
    }

    public void setUid(String uid){
        this.uid=uid;
    }

    public void addSubscribe(String subscribe){
        compSubscribe.add(subscribe);
    }

    public String getUid(){
        return uid;
    }

    public static boolean validateUID(String userId){
        return userId.length() == 9 && isNumber(userId);
    }

    public Session getSession(){
        return session;
    }

    private static boolean isNumber(String num){
        if(num.isEmpty()) return false;
        for(char n: num.toCharArray()){
            if(!Character.isDigit(n))
                return false;
        }
        return true;
    }

    public boolean searchTicker(String ticker){
        for(String c:compSubscribe){
            if(Objects.equals(ticker, c)) {
                return true;
            }
        }
        return false;
    }
}

