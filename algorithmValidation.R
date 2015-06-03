#Script written by Kanishk Asthana kasthana@eng.ucsd.edu
# Script for testing the validity of the algorithm that I have designed.
# The basic logic of the validation is as follows:

require("genomeIntervals")
loopsList=read.table("GSE63525_IMR90_HiCCUPS_looplist.txt", header=TRUE)

print("Number of loops in X chromosome")
print(sum(loopsList$chr1=="X"))

loopsX=loopsList[loopsList$chr1=="X",]

#In the algorithm we are testing, each pair of observations have to be such that one of them is in the positive strand 
# and the other one is one the negative strand. So the only way we can test the algorithms is if we randomly assign loopBoundaries
# that are in a pair with positive and negative signs

#Creating loop Pairs
loopPairs=as.matrix(loopsX[,c(2,5)])

#Creating a distribution where we designate either the start of the end of the loop to be on the positive strand

strand=sample(0:1,nrow(loopPairs),replace=TRUE)

for(i in 1:nrow(loopPairs)){
  if(strand[i]==0){
    loopPairs[i,1]=-1*loopPairs[i,1]
  }
  else{
    loopPairs[i,2]=-1*loopPairs[i,2]
  }
}

#Now the matrix loopPairs has either the start or end of each row randomly assigned to the positive and negative strands
loopBoundaries=as.vector(loopPairs)

#Ordering boundaries in ascending order of absolute value:
loopBoundaries=loopBoundaries[order(abs(loopBoundaries))]

#Now we have the vector we want to export to test our algorithm with
write.table(loopBoundaries,file="processedLoopBoundaries.txt",quote=FALSE, col.name=FALSE,row.names=FALSE, sep="\t")

#Reading predicted Loops from algorithm
predictedLoops = read.csv("predictedLoopsFromLoopBoundaries2.csv",header=TRUE);

shift =0

loopsXStartRange=cbind((loopsX$x1+shift),(loopsX$x2+shift));
XStartRange=Intervals(loopsXStartRange);
loopsXEndRange=cbind((loopsX$y1+shift),(loopsX$y2+shift));
XEndRange=Intervals(loopsXEndRange);

predictedLoopsXStartRange=cbind((predictedLoops$START-5000),(predictedLoops$START+5000));
predictedStartInterval=Intervals(predictedLoopsXStartRange);

predictedLoopsXEndRange=cbind((predictedLoops$END-5000),(predictedLoops$END+5000));
predictedEndInterval=Intervals(predictedLoopsXEndRange);

#Vector of CTCF Loops Start Prediction positions that match with predicted start vector for loops
startListVector=as.list(interval_overlap(XStartRange,predictedStartInterval));

#Vector of CTCF loops End prediction positions that match with predicted end vector for loops
endListVector=as.list(interval_overlap(XEndRange,predictedEndInterval));

#A vector to store index of commond predicted and experiemental loops
commonLoops=vector("list",length=length(startListVector));

for(i in 1:length(startListVector)){
  lst1=unlist(startListVector[i]);
  lst2=unlist(endListVector[i]);
  intrsct=as.list(intersect(lst1,lst2))
  commonLoops[i]=as.list(NA)
  if(length(intrsct)>0){
    commonLoops[i]=as.list(intersect(lst1,lst2))
  }
  
}

#Number acurately predicted for experimental data
logicalVectorOfCommonLoops=!sapply(commonLoops,is.na)

print("Number of Common Loops:");
print(sum(logicalVectorOfCommonLoops))

print("Percentage shown to be correct:")
print(sum(logicalVectorOfCommonLoops)/nrow(loopsX))


