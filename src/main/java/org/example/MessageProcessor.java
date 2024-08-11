package org.example;

import org.json.JSONObject;
import java.util.List;
import java.util.Map;

public class MessageProcessor {
    private Map<String, List<Integer>> volumeMap;
    private WritetoFile writetofile;

    public MessageProcessor(Map<String, List<Integer>> volumeMap, WritetoFile writetoFile){
        this.volumeMap=volumeMap;
        this.writetofile=writetoFile;
    }

    public void processMessage(String message, String ticker) {
        JSONObject m = new JSONObject(message);
        int volume = 0;
        if (m.has("volume") && !m.getString("volume").equals("#")) {
            volume = m.getInt("volume");
        }
        List<Integer> volumes=volumeMap.get(ticker);
        volumes.add(volume);
        if (volumes.size() == 5) {
            double average=getAverage(volumes);
            String averageMessage="Average of the last 5 messages is:" + average;
            WritetoFile.writefile(ticker,averageMessage);
            volumes.clear();
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
