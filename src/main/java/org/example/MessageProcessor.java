package org.example;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MessageProcessor {
    private Map<String, Company> companiesmap;
    private WritetoFile writetofile;



    public MessageProcessor(Map<String, Company> companiesmap ){
        this.companiesmap = companiesmap;
    }

    public void processMessage(String message, String ticker) throws IOException {
        JSONObject m = new JSONObject(message);
        //LiveData.sendMessage(message);
        int volume = 0;
        if (m.has("volume") && !m.getString("volume").equals("#")) {
            volume = m.getInt("volume");
        }
        Company company=companiesmap.get(ticker);
        company.addVolume(volume);
        if(company.getSize()==5) {
            double avg = company.getAvg();
            String outmsg = "The average volume for " + ticker+ " is: " + avg;
            LiveData.sendMessage(outmsg);
        }
    }


    public String getTicker(String message){
        JSONObject j = new JSONObject(message);
        String topic= j.getString("topic");
        String []parts= topic.split("\\.");
        String ticker=parts[1];
        return ticker;

    }
}
