package org.example;

import org.json.JSONObject;
import java.util.List;
import java.util.Map;

public class MessageProcessor {
    private Map<String, Company> companiesmap;
    private WritetoFile writetofile;


    public MessageProcessor(Map<String, Company> companiesmap ){
        this.companiesmap = companiesmap;
    }

    public void processMessage(String message, String ticker) {
        JSONObject m = new JSONObject(message);
        int volume = 0;
        if (m.has("volume") && !m.getString("volume").equals("#")) {
            volume = m.getInt("volume");
        }
        Company company=companiesmap.get(ticker);
        company.addVolume(volume);
        if(company.getSize()==5) {
            double avg = company.getAvg();
            String outmsg = "The average volume for" + ticker+ " is: " + avg;
            LiveData.broadcastMessage(outmsg);
        }
    }

    private double getAverage(List<Integer> volumes){
        int sum=0;
        for(int v:volumes)
            sum+=v;
        return sum/5.0;
    }

    public String getTicker(String message){
        JSONObject j = new JSONObject(message);
        String topic= j.getString("topic");
        String []parts= topic.split("\\.");
        String ticker=parts[1];
        return ticker;

    }
}
