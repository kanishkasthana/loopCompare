/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loopprediction;
import java.io.*;
import java.util.*;

/**
 *
 * @author kanis_000
 */
public class LoopPrediction {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            System.out.println(System.getProperty("user.dir"));
            List<String> inputs= new <String>ArrayList();
            File newFile=new File("processedPeaks.txt");
            FileReader fileReader=new FileReader(newFile);
            BufferedReader reader=new BufferedReader(fileReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
             inputs.add(line);
            }
            
            List <node>allVertices=new <node>ArrayList();
            for(String text:inputs){
                long peakNumber=Long.parseLong(text);
                allVertices.add(new node(peakNumber));
            }
            
        
        }
        catch(Exception e){
         e.printStackTrace();
        }
    }
    
}
