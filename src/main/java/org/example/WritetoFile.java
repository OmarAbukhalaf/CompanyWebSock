package org.example;
import java.io.FileWriter;
import java.io.IOException;



public class WritetoFile {

        public static void writefile(String ticker, String message) {
            try (FileWriter writer = new FileWriter(ticker + ".txt",true)) {
                writer.write(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

