package org.example;

import org.json.JSONObject;
import java.io.IOException;
import java.util.Map;

import static org.example.LiveData.subscribe;


public class MessageProcessor {
    private Map<String, Company> companiesmap;

    public MessageProcessor(Map<String, Company> companiesmap){
        this.companiesmap = companiesmap;
    }

    public void processMessage(String message, String ticker) throws IOException {
        JSONObject m = new JSONObject(message);
        System.out.println("Logging: " + message);
        int volume = 0;
        if (m.has("volume") && !m.getString("volume").equals("#")) {
            volume = m.getInt("volume");
        }
        Company company=companiesmap.get(ticker);
        company.addVolume(volume);
        if(company.getSize()==1) {
            double avg = company.getAvg();
            String outmsg = "The average volume for " + ticker+ " is: " + avg;
            //outputmsg(outmsg,ticker);
            System.out.println("Subscribe= " + subscribe);
            System.out.println("Ticker= " + ticker);
            System.out.println("outmsg= " + message);
            LiveData.sendMessage(ticker,message);
            company.clearVolume();

        }
    }

    public void outputmsg(String message, String ticker){
        System.out.println("Subscribe= " + subscribe);
        System.out.println("Ticker= " + ticker);
            System.out.println("outmsg= " + message);
            LiveData.sendMessage(ticker,message);
        }
}
