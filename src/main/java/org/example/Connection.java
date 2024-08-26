package org.example;

import org.eclipse.jetty.websocket.api.Session;

public class Connection {
    private Session session;
    private String uid;
    private String compSubscribe;


    public Connection(Session session,String uid){
        this.session=session;
        this.uid=uid;
    }

    public void setUid(String uid){
        this.uid=uid;
    }

    public void setSubscribe(String subscribe){
        this.compSubscribe=subscribe;
    }

    public String getUid(){
        return uid;
    }
    public String getCompSubscribe(){
        return compSubscribe;
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
}
