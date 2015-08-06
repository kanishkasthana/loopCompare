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
public class edge {
    public static List <edge> alledges=new <edge>ArrayList();
    public static node[] allnodes;
    node parent;
    node child;
    int weight;
    String edgeString=null;
    boolean traversed=false;
    String initialEdgeString=null;
    String terminalEdgeString=null;
    char edgeChar;
    
    public edge(String edgedata){
        
        StringTokenizer fullString=new StringTokenizer(edgedata,"->");
        int parentNodeValue=Integer.parseInt(fullString.nextToken());
        StringTokenizer childString=new StringTokenizer(fullString.nextToken(),":");
        int childNodeValue=Integer.parseInt(childString.nextToken());
        int weight=Integer.parseInt(childString.nextToken());
        for(int i=0;i<this.allnodes.length;i++){
            if(allnodes[i].getNodeNumber()==parentNodeValue){
                this.parent=allnodes[i];
            }
            if(allnodes[i].getNodeNumber()==childNodeValue){
                this.child=allnodes[i];
            }
        }
        
        parent.addChild(child);
        child.addParent(parent);
        parent.addEdge(this);
        child.addEdge(this);
        this.weight=weight;
        this.alledges.add(this);
    }
     
    public edge(node parent, node child, String edgeString){
        this.parent=parent;
        this.child=child;
        this.edgeString=edgeString;
        this.parent.addChild(child);
        this.child.addParent(parent);
        this.parent.addEdge(this);
        this.child.addEdge(this);
        this.alledges.add(this);
        this.parent.addOutgoingEdge(this);
        this.child.addIncomingEdge(this);
    }
    
    public edge(node parent, node child, char edgeChar){
        this.parent=parent;
        this.child=child;
        this.edgeChar=edgeChar;
        this.parent.addChild(child);
        this.child.addParent(parent);
        this.parent.addEdge(this);
        this.child.addEdge(this);
        this.alledges.add(this);
        this.parent.addOutgoingEdge(this);
        this.child.addIncomingEdge(this);
    }
    
    public edge(node parent, node child, String initialEdgeString,String terminalEdgeString){
        
        this.parent=parent;
        this.child=child;
        this.initialEdgeString=initialEdgeString;
        this.terminalEdgeString=terminalEdgeString;
        this.parent.addChild(child);
        this.child.addParent(parent);
        this.parent.addEdge(this);
        this.child.addEdge(this);
        this.alledges.add(this);
        this.parent.addOutgoingEdge(this);
        this.child.addIncomingEdge(this);
        
    }
    
    public edge(String initialEdgeString,String terminalEdgeString){
        
        int k=initialEdgeString.length();
        String initialPrefixString=initialEdgeString.substring(0,k-1);
        String terminalPrefixString=terminalEdgeString.substring(0,k-1);
        String initialSufixString=initialEdgeString.substring(1);
        String terminalSufixString=terminalEdgeString.substring(1);
        
        for(int i=0;i<node.allnodes.size();i++)
        {
          if(node.allnodes.get(i).isSame(initialPrefixString, terminalPrefixString)){
              this.parent=node.allnodes.get(i);
              break;
          }  
        }
        
        for(int i=0;i<node.allnodes.size();i++)
        {
          if(node.allnodes.get(i).isSame(initialSufixString, terminalSufixString)){
              this.child=node.allnodes.get(i);
              break;
          }  
        }
        
        this.initialEdgeString=initialEdgeString;
        this.terminalEdgeString=terminalEdgeString;
        this.parent.addChild(child);
        this.child.addParent(parent);
        this.parent.addEdge(this);
        this.child.addEdge(this);
        this.alledges.add(this);
        this.parent.addOutgoingEdge(this);
        this.child.addIncomingEdge(this);
    
    }
    
    public edge(node parent,node child,int weight){
        this.parent=parent;
        this.child=child;
        this.weight=weight;
        this.parent.addChild(child);
        this.child.addParent(parent);
        this.parent.addEdge(this);
        this.parent.addOutgoingEdge(this);
        this.child.addEdge(this);
        this.child.addIncomingEdge(this);
        this.alledges.add(this);
    }
    
    /*
    public edge(int i1,int j1,int i2,int j2,int weight){
        for(int count=0;count<this.allnodes.length;count++){
            if(allnodes[count].getI()==i1 && allnodes[count].getJ()==j1)
        }
    }
    */
    
    public char getEdgeChar(){
        return edgeChar;
    }
    
    public String getInitialEdgeString(){
        return initialEdgeString;
    }
    
    public String getTerminalEdgeString(){
        return terminalEdgeString;
    }
    
    public String getEdgeString(){
        return edgeString;
    }
    
    public node getParent(){
        return parent;
    }
    
    public node getChild(){
        return child;
    }
    
    public int getWeight(){
        return weight;
    }
    
    public void traversed(){
        this.traversed=true;
    }
    
    public boolean isTraversed(){
        return this.traversed;
    }
    
    public void reset(){
        this.traversed=false;
    }
    
    public static void resetAllEdges(){
        for(int i=0;i<alledges.size();i++){
            edge currentEdge=alledges.get(i);
            currentEdge.reset();
        }
    }
    
}
