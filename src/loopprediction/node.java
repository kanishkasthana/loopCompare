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
public class node {
    public static int upper=1;
    public static int lower=-1;
    public static int middle=0;
    
    
    private String text=null;
    private List frequentPatterns=null;
    int[] frequencyArray=null;
    private String reverseText=null;
    public static int red=1;
    public static int blue=2;
    public static int purple=3;
    public static int gray=0;
    
    public static List<node> allnodes=new <node>ArrayList();
    public static List<edge> alledges=new <edge>ArrayList();
    int matrixlevel;
    int value;
    int i;
    int j;
    int k;
    List <edge>edges=new <edge>ArrayList();
    List <edge>outgoingEdges=new <edge>ArrayList();
    List <edge>incomingEdges=new <edge>ArrayList();
    int nodeDepth;//To represent how deep this node is in the tree
    public static String genome;
    
    int score;
    List <node>parents=new <node>ArrayList();
    List <node>children=new <node>ArrayList();
    node backtrackNode=null;
    String nodeString=null;
    String initial=null;
    String terminal=null;
    List inputs=null;
    int colour=gray;
    long peakNumber=0;
    
    public node(int value){
        this.value=value;
        allnodes.add(this);
    }
    
    public node(Long value){
        this.peakNumber=value;
        allnodes.add(this);
    }
    
    public node(int value,List inputs,int depth){
        this.value=value;
        this.inputs=inputs;
        this.nodeDepth=depth;
        allnodes.add(this);
    }
    
    public node(int i,int j){
        this.i=i;
        this.j=j;
        allnodes.add(this);
    }
    
    /*
    public node(int i,int j,int matrixlevel){
        this.i=i;
        this.j=j;
        this.matrixlevel=matrixlevel;
        allnodes.add(this);
        this.setScore(0);
    }*/
    
    
    public node(int i,int j, int k){
        this.i=i;
        this.j=j;
        this.k=k;
        allnodes.add(this);
        this.setScore(0);
    }
    
    public node(String nodeString){
        this.nodeString=nodeString;
        allnodes.add(this);
    }
    
    public node(String initialString,String terminalString){
        this.initial=initialString;
        this.terminal=terminalString;
        allnodes.add(this);
    }
    
    public static void addPairedToNodes(String initialString,String terminalString){
        boolean presentInGraph=false;
        for(int i=0;i<node.allnodes.size();i++){
            if(node.allnodes.get(i).isSame(initialString, terminalString)){
                presentInGraph=true;
                break;
            }
        }
        if(!presentInGraph){
            node newNode=new node(initialString,terminalString);
        }
    }
    
    public static node addToNodes(int nodenumber){
        boolean presentInGraph=false;
        for(int i=0;i<node.allnodes.size();i++){
            if(node.allnodes.get(i).getNodeNumber()==nodenumber){
                presentInGraph=true;
                return node.allnodes.get(i);
            }
        }
        
        if(!presentInGraph){
            node newNode=new node(nodenumber);
            return newNode;
        }
        return null;
    }
    
    public void setColor(int colour){
        this.colour=colour;
    }
    
    public long getPeakNumber(){
        return this.peakNumber;
    }
    
    public int getColor(){
        return colour;
    }
    
    public static void setGenome(String genome){
        node.genome=genome;
    }
    
    public int getDepth(){
        return this.nodeDepth;
    }
    
    public String getInitialString(){
        return this.initial;
    }
    
    public String getTerminalString(){
        return this.terminal;
    }
    
    public String getNodeString(){
        return this.nodeString;
    }
    
    public int getNodeNumber(){
        return value;
    }
    
    public int getMatrixLevel(){
        return this.matrixlevel;
    }
    
    public int getI(){
        return this.i;
    }
    
    public int getJ(){
        return this.j;
    }
    
    public int getK(){
        return this.k;
    }
    public List<edge> getEdges(){
        return edges;
    }
    
    public void setScore(int score){
        this.score=score;
    }
    public int getScore(){
        return this.score;
    }
    
    public void addChild(node child){
        children.add(child);
    }
    
    public void addParent(node parent){
        parents.add(parent);
    }
    
    public List<node> getChildren(){
        return this.children;
    }
    
    public List<node> getParents(){
        return this.parents;
    }
    
    public void addEdge(edge newedge){
        this.edges.add(newedge);
    }
    
    public void addOutgoingEdge(edge e){
        this.outgoingEdges.add(e);
    }
    
    public void addIncomingEdge(edge e){
        this.incomingEdges.add(e);
    }
    
    public List<edge> getOutgoingEdges(){
        return this.outgoingEdges;
    }
    
    public List<edge> getIncomingEdges(){
        return this.incomingEdges;
    }
    
    public node getBacktrackNode(){
        return backtrackNode;
    }
    
    public edge getEdge(node child){
      edge e=null;
        for(int i=0;i<this.getEdges().size();i++){
            if((  (edge)this.getEdges().get(i)  ).getChild().equals(child))
                e= (edge) this.getEdges().get(i);
        }
      return e;
    }
    
    public edge getEdgeParent(node parent){
      edge e=null;
        for(int i=0;i<this.getEdges().size();i++){
            if((  (edge)this.getEdges().get(i)  ).getParent().equals(parent))
                e= (edge) this.getEdges().get(i);
        }
      return e;    
    }
    //Wow, who would have thought the performance improvements you could get from reorganizing your code, its incredible!
    public void computeScores(node sinknode, node sourcenode){
      if(!this.getParents().isEmpty()){
          int[] scores=new int[this.getParents().size()];
          node maxParent=(node)this.getParents().get(0);         
          int maxScore=maxParent.getScore();
          if(!this.equals(sinknode))
              maxScore+=this.getEdgeParent(maxParent).getWeight();
          else{
              if(!maxParent.equals(sourcenode))
                  maxScore+=maxParent.getEdge(this).getWeight();
          }

          for(int i=0;i<this.getParents().size();i++){
              node parent=(node)this.getParents().get(i);
              int score=parent.getScore();
              if(!this.equals(sinknode))  
                score+=this.getEdgeParent(parent).getWeight();
              else{
                  if(!parent.equals(sourcenode))
                      score+=parent.getEdge(this).getWeight();
              }
                if(score>maxScore){
                      maxScore=score;
                      maxParent=parent;
                }
          }
          this.setScore(maxScore);
          this.backtrackNode=maxParent;
      }     
    }
    
    public static void sort(){
        allnodes=mergeSort(allnodes);
    }
    
    public static List mergeSort(List<node> list){
        if(list.size()==1){
            return list;
        }
        int firstHalfIndex=list.size()/2;
        List <node>firstHalf=list.subList(0, firstHalfIndex);
        List <node>secondHalf=list.subList(firstHalfIndex,list.size());
        List <node>sortedFirstHalf=mergeSort(firstHalf);
        List <node>sortedSecondHalf=mergeSort(secondHalf);
        List <node>sortedList=merge(sortedFirstHalf,sortedSecondHalf);
        
        return sortedList;
    }
    
    public static List merge(List <node>list1,List <node>list2){
        List <node>sortedList=new <node>ArrayList();
        int firstStringPos=0,secondStringPos=0;
        String current1,current2;
        
        while(firstStringPos<list1.size() && secondStringPos<list2.size()){
            current1=list1.get(firstStringPos).getNodeString();
            current2=list2.get(secondStringPos).getNodeString();
            if(current1.compareTo(current2)<0){
                sortedList.add(list1.get(firstStringPos));
                firstStringPos++;
            }
            else{
                sortedList.add(list2.get(secondStringPos));
                secondStringPos++;
            }
        }
        
        
        for(int i=firstStringPos;i<list1.size();i++){
            sortedList.add(list1.get(i));
        }
        
        
        for(int i=secondStringPos;i<list2.size();i++){
            sortedList.add(list2.get(i));
        }
        
        return sortedList;
    }
    
    public boolean hasUnexploredEdges(){
        for(int i=0;i<getOutgoingEdges().size();i++)
            if(!getOutgoingEdges().get(i).isTraversed())
                return true;
        
        return false;
    }
    
    public boolean isBalanced(){
        if(this.getOutgoingEdges().size()==this.getIncomingEdges().size())
            return true;
        else 
            return false;
    }
    
    public int balance(){
        return getOutgoingEdges().size()-getIncomingEdges().size();
    }
    
    public boolean isSame(String initial,String terminal){
        if(this.initial.equals(initial) && this.terminal.equals(terminal))
            return true;
        else return false;
    }
    
    public void printPair(){
        System.out.println(this.initial+"|"+this.terminal);
    }
    
    public void createSufixTrie(){
      if(!inputs.isEmpty())  
      
      {
        
        List startsWithA=new ArrayList();
        List startsWithC=new ArrayList();
        List startsWithG=new ArrayList();
        List startsWithT=new ArrayList();
        int countA=0,countC=0,countG=0,countT=0;
        
        for(int i=0;i<inputs.size();i++){
            char first=genome.substring((int)inputs.get(i)).charAt(0);
            int suffix=((int)inputs.get(i))+1;
            if(first=='A'){
                countA++;
                if(suffix<genome.length())
                    startsWithA.add(suffix);
            }
            
            else if(first=='C'){
                countC++;
                if(suffix<genome.length())
                    startsWithC.add(suffix);
            }
            
            else if(first=='G'){
                countG++;
                if(suffix<genome.length())
                    startsWithG.add(suffix);
            }
            
            else if(first=='T'){
                countT++;
                if(suffix<genome.length())
                    startsWithT.add(suffix);
            }
 
        }
        
        if(countA!=0){
            node child=new node(allnodes.size(),startsWithA,this.getDepth()+1);
            alledges.add(new edge(this,child,'A'));
            child.createSufixTrie();
        }
        
        if(countC!=0){
            node child=new node(allnodes.size(),startsWithC,this.getDepth()+1);
            alledges.add(new edge(this,child,'C'));
            child.createSufixTrie();
        }
        
        if(countG!=0){
            node child=new node(allnodes.size(),startsWithG,this.getDepth()+1);
            alledges.add(new edge(this,child,'G'));
            child.createSufixTrie();
        }
        
        if(countT!=0){
            node child=new node(allnodes.size(),startsWithT,this.getDepth()+1);
            alledges.add(new edge(this,child,'T'));
            child.createSufixTrie();
        }
        
      }
      
      else 
          return;
    }
    
    public edge charPresentInEdges(char c){
        edge matchedEdge=null;
        
        for(int i=0;i<this.outgoingEdges.size();i++){
            edge currentEdge=this.outgoingEdges.get(i);
            if(currentEdge.getEdgeChar()==c){
                matchedEdge=currentEdge;
                break;
            }
        }
        
        return matchedEdge;
    }
    
    public void deleteEdge(edge e){
        this.outgoingEdges.remove(e);
        this.edges.remove(e);
        edge.alledges.remove(e);
        this.incomingEdges.remove(e);
    }
    
    public boolean isAllChildrenColourd(){
        if(!this.children.isEmpty())
        {
            boolean allColoured=true;
            for(int i=0;i<this.getChildren().size();i++){
                node child=this.getChildren().get(i);
                if(child.getColor()==gray){
                    allColoured=false;
                    break;
                }
            }    
            return allColoured;
        }
        else{
            return false;
        }
    }
    public static boolean hasRipeNodes(){
       boolean ripe=false;
       
       for(int i=0;i<allnodes.size();i++){
           if(allnodes.get(i).getColor()==gray){
               ripe=true;
               break;
           }
       }
       
       return ripe;
    }
    
    public void decideColour(){
        int redCount=0;
        int blueCount=0;
        for(int i=0;i<this.children.size();i++){
            node child=this.children.get(i);
            if(child.getColor()==red){
                redCount++;
            }
        
            if(child.getColor()==blue){
                blueCount++;
            }
        }
        
        if(redCount==this.children.size()){
            this.setColor(red);
        }
        else if(blueCount==this.children.size()){
            this.setColor(blue);
        }
        else{
            this.setColor(purple);
        }
    }
}
