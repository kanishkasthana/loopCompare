/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loopprediction;
import java.io.*;
import java.util.*;
import java.lang.Math;

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
            List <edge>allEdges=new <edge>ArrayList();
            boolean up=false;
            boolean down=false;
            node prev=null;
            int count=0;
            for(node vertex:allVertices){
                count++;
                if(vertex.getPeakNumber()>0){
                    if(down){
                        allEdges.add(new edge(vertex,prev,0));
                        prev=null;
                        down=false;
                    }
                    else if(!up){
                        up=true;
                        prev=vertex;
                    }
                }
                else if(vertex.getPeakNumber()<=0){
                    if(up){
                        allEdges.add(new edge(prev,vertex,0));
                        prev=null;
                        up=false;
                    }
                    else if(!down){
                        down=true;
                        prev=vertex;
                    }
                    
                }
            }
            
            //Printing out Start and End predicted Loops
            PrintWriter out= new PrintWriter(new FileWriter("predictedLoops.txt"));
            out.println("START\tEND");
            for(edge e:allEdges){
                
                if(Math.abs(e.getParent().getPeakNumber())<=Math.abs(e.getChild().getPeakNumber())){
                    out.print(Math.abs(e.getParent().getPeakNumber()));
                    out.print("\t");
                    out.println(Math.abs(e.getChild().getPeakNumber()));
                }
                else{
                    out.print(Math.abs(e.getChild().getPeakNumber()));
                    out.print("\t");
                    out.println(Math.abs(e.getParent().getPeakNumber()));
                }
            }
            
            out.close();
        
        }
        catch(Exception e){
         e.printStackTrace();
        }
    }
    
}
