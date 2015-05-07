#Script written by Kanishk Asthana kasthana@eng.ucsd.edu
require("intervals")
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

print(sum(loopsList$chr1=="X"))
print(sum(arrowheadDomains$chr1=="X"))

CTCFXdist=originalFile[originalFile$V1=="chrX",]

#No negative peaks
print(sum((CTCFXdist$V3-CTCFXdist$V2)<0))

orderedCTCFXPeaks=CTCFXdist[order(CTCFXdist$V2),]

#Distribution of distance between peaks in the X chromosome. Perhaps some peaks can be merged.
#I am not sure of that yet. Wow R is nice for handling a lot of data dude.


peakdistances=diff(orderedCTCFXPeaks$V2);
hist(peakdistances,500)
peaklengths=orderedCTCFXPeaks$V3-orderedCTCFXPeaks$V2
hist(peaklengths,500)

#So peaks merging with each other or that the same peak is counted as two peaks is not really a problem
print(sum(peakdistances<5000))

domainLengths=arrowheadDomains$x2-arrowheadDomains$x1

hist(domainLengths,100)

#Increasing range of CTCF peaks for comparison with topological domain boundaries

increasedRange=cbind((orderedCTCFXPeaks$V2-1000),(orderedCTCFXPeaks$V3+1000))

#A constant shift of 2000 units seems to work
val=((increasedRange[,2]-increasedRange[,1])-peaklengths)

domainsX=arrowheadDomains[arrowheadDomains$chr1=="X",c(2,3)]

domainsX=domainsX[order(domainsX$x1),]

uniquedomainsX=unique(as.numeric(cbind(domainsX$x1,domainsX$x2)))


#Taking the same numbers twice to show that the intervals are actually wait I can extend the topological domains instead of 
#The chip seq peaks right. Hmm that kind of makes sense. Lets do this first and see what we get.
intervalsuniqueDomainsX=Intervals(cbind((uniquedomainsX-5000),(uniquedomainsX+5000)))
intervalspeaksX=Intervals(increasedRange)
percentageofboundariesatCTCFpeaks=length(unique(unlist(interval_overlap(intervalspeaksX,intervalsuniqueDomainsX))))/length(uniquedomainsX)
print(percentageofboundariesatCTCFpeaks*100)


homerPeaks=read.table("IMR90CTCFPeaks.txt")
names(homerPeaks)<-c("PeakID","chr","start","end","strand","Normalized_Tag_Count","focus_ratio","findPeaks_Score","Fold_ChangevsLocal","p-valuevsLocal","ClonalFoldChange")







