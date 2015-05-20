#Script written by Kanishk Asthana kasthana@eng.ucsd.edu

require("genomeIntervals")

homerPeaks=read.table("IMR90CTCFPeaks.txt")
names(homerPeaks)<-c("PeakID","chr","start","end","strand","Normalized_Tag_Count","focus_ratio","findPeaks_Score","Fold_ChangevsLocal","p-valuevsLocal","ClonalFoldChange")

intersect=read.table("temp3.bed")
print(nrow(intersect))
originalFile=read.table("ENCFF001VLP.narrowPeak")
print(nrow(originalFile))

#Finding number of Peaks on ChromosomeX
totalCTCF=(sum(originalFile$V1=="chrX"))
print("CTCF Motifs in X Chromosome")
print(totalCTCF)
#TO DO CHECK DISTRIBUTION OF LOOPS IN X CHROMOSOME

arrowheadDomains=read.table("GSE63525_IMR90_Arrowhead_domainlist.txt", header=TRUE)
loopsList=read.table("GSE63525_IMR90_HiCCUPS_looplist.txt", header=TRUE)

print("Number of loops in X chromosome")
print(sum(loopsList$chr1=="X"))
print("Number of Topological Domains in X chromosome")
print(sum(arrowheadDomains$chr1=="X"))
CTCFXdist=originalFile[originalFile$V1=="chrX",]

#No negative peaks
print(sum((CTCFXdist$V3-CTCFXdist$V2)<0))

orderedCTCFXPeaks=CTCFXdist[order(CTCFXdist$V2),]

#Distribution of distance between peaks in the X chromosome. Perhaps some peaks can be merged.
#I am not sure of that yet. Wow R is nice for handling a lot of data dude.

peakdistances=diff(orderedCTCFXPeaks$V2);
hist(log10(peakdistances),500)
peaklengths=orderedCTCFXPeaks$V3-orderedCTCFXPeaks$V2
hist(peaklengths,500)

#So peaks merging with each other or that the same peak is counted as two peaks is not really a problem
print(sum(peakdistances<5000))

domainLengths=arrowheadDomains$x2-arrowheadDomains$x1
hist(log10(domainLengths),100)

#Increasing range of CTCF peaks for comparison with topological domain boundaries
increasedRange=cbind((orderedCTCFXPeaks$V2-7500),(orderedCTCFXPeaks$V3+7500))
#Replacing peaks ENCODE peaks with HOMER peaks
#homerPeaksX=homerPeaks[homerPeaks$chr=="chrX",]
#increasedRange=cbind((homerPeaksX$start-7500),(homerPeaksX$end+7500));

#A constant shift of 2000 units seems to work
#val=((increasedRange[,2]-increasedRange[,1])-peaklengths)
domainsX=arrowheadDomains[arrowheadDomains$chr1=="X",c(2,3)]
domainsX=domainsX[order(domainsX$x1),]
uniquedomainsX=unique(as.numeric(cbind(domainsX$x1,domainsX$x2)))

#Uncomment the result below to check what percentage enrichement you see for the control set (which is boundaries have been shifted)
#uniquedomainsX=uniquedomainsX+1000000

#Taking the same numbers twice to show that the intervals are actually wait I can extend the topological domains instead of 
#The chip seq peaks right. Hmm that kind of makes sense. Lets do this first and see what we get.
#+/- 10kb chosen for the domain and loop boundaries because this is the resolution at which the domain boundaries were
#actually called, which is pretty freaking intersting dude.
intervalsuniqueDomainsX=Intervals(cbind((uniquedomainsX-10000),(uniquedomainsX+10000)))
intervalspeaksX=Intervals(increasedRange)
percentageofboundariesatCTCFpeaks=length(unique(unlist(as.list(interval_overlap(intervalspeaksX,intervalsuniqueDomainsX)))))/length(uniquedomainsX)
print("Percentage of TAD boundaries that overlap with CTCF peaks")
print(percentageofboundariesatCTCFpeaks*100)

#Next check how many of the loops overlap with the ctcf sites.
loopsX=loopsList[loopsList$chr1=="X",]
#print("Number of loops in X chromosome:") Doing the same thing as above to get the overlap stuff.
uniqueLoopsX=unique(as.numeric(cbind(loopsX$x1,loopsX$x2)))
uniqueLoopsX=uniqueLoopsX[order(uniqueLoopsX)]

#Uncomment the line below to see what result you get for a control set (which is boundaries have been shifted)
uniqueLoopsX=uniqueLoopsX + 1000000
uniqueLoopsXRange=cbind((uniqueLoopsX-10000),(uniqueLoopsX+10000))
intervaluniqueLoopsX=Intervals(uniqueLoopsXRange)
#Getting percentage of unique loop sites that actually overlap with CTCF sites:
# The as.list function here is really important. Because of that we can use the lapply function in R
# Wow, I am really starting to get the hang of R. Its kind of a nice language once you know all the basics
# I am not sure how Python would be any different, but this is really freaking nice and fast. Also you can create markdown documents really quickly
# The nice thing about python is that is has a lot of other stuff going for it as well, like django and stuff. Which is pretty damn cool dude

percentofLoopsAtCTCFPeaks=length(unique(unlist(as.list(interval_overlap(intervalspeaksX,intervaluniqueLoopsX)))))/length(uniqueLoopsX)
print("Percentage of Loops boundaries that coincide with CTCF peaks:")
#These findings are really not reproducible from that Suhas rao paper
print(percentofLoopsAtCTCFPeaks*100)

# You need a quantitative way of checking your belief. The idea is is it significantly enriched or not. Yes, i think if you do a statistical test
# You can check which ones are statistically enriched or not.


#Trying to check whether there is an overlap between the two lists of lists
# So every element of the following list is a list itself. If the list element lst1 from list1 has a single 
# element in common with the list element lst2 from list2 then the the intersection of the two lists will be non-empty
# int is a vector that stores the value NA if the intersection has no elements and as an intersection list if there is a single overlap
# this is a way we can compare whether the start and end points predicted by my algorithm coincide with the start and end sites
# of the already predicted loops.

list=as.list(interval_overlap(intervalspeaksX,intervaluniqueLoopsX));

int=vector("list",length=length(list))

for(i in 1:length(list)){
  lst1=unlist(list[i])
  lst2=unlist(list[i])
  intrsct=as.list(intersect(lst1,lst2))
  int[i]=as.list(NA)
  if(length(intrsct)>0){
    int[i]=as.list(intersect(lst1,lst2))
  }
}

logicalVectorOfElementsThatOverlap=!sapply(int,is.na)

#Processing Peaks for algorithms implemented in Java https://github.com/kanishkasthana/loopPrediction
homerPeaksX=homerPeaks[homerPeaks$chr=="chrX",]

homerPeaksX=homerPeaksX[order(homerPeaksX$start),];

processedPeaks=apply(cbind(homerPeaksX$start,homerPeaksX$strand),1,function(peak){
  if(peak[2]==2){
    return(peak[1])
  }
  else{
    return(peak[1]*-1)
  }
});

write.table(processedPeaks,file="processedPeaks.txt",quote=FALSE, col.name=FALSE,row.names=FALSE, sep="\t")
