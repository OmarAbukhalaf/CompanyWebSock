package org.example;
import java.util.ArrayList;
import java.util.List;

public class Company{
    private String ticker;
    private List<Integer> volumes;

    public Company(String ticker){
        this.ticker=ticker;
        this.volumes=new ArrayList<>();
    }
    public String getTicker(){
        return ticker;
    }

    public void addVolume(int volume) {
        volumes.add(volume);
    }

    public double getAvg(){
        int sum=0;
        for(int v:volumes){
            sum+=v;
        }
        return sum/5.0;
    }

    public int getSize(){
        return volumes.size();
    }
}
