package org.example;

import java.util.ArrayList;
import java.util.List;

import org.example.comparators.WeightComparator;
import org.example.nodes.HILNode;
import org.example.utils.ARFFReader;
import org.example.utils.DistanceCalculator;
import org.example.utils.MeasureCalculator;
import weka.core.Instance;
import weka.core.Instances;

/*
 detects outliers by calculating weight of each data point, the weight is the sum of distances
  between one point to its k-nearest neighbors. Thus, those data points who have the higher weight are
  more likely to be the outliers.
  1. calculating distances of any pair of points.
  2. getting K<-nearest neighbors of each point.
  3. counting the weight of each point.
  4. ranking the points in terms of weights, the top-N points are detected as outliers.
 */
public class HilOut {
	
	private Instances dataset;
	
	public static final int K = 5;
	// top n outliers
	public static final double N = 0.15;
	
	private static final  List<HILNode> nodeset = new ArrayList<>();
	
	private static final List<double[]> lsDistance = new ArrayList<>();
	
	public HilOut(String path){
		
		nodeset.clear();
		lsDistance.clear();
		
		ARFFReader reader = new ARFFReader(path);
		dataset = reader.getDataset();
		for(int i=0; i<dataset.numInstances(); i++){
			HILNode node = new HILNode(dataset.get(i));
			nodeset.add(node);
		}
		
		calculateKNeighbors();
		
		rankingByWeights();
	}
	
	public Instance getIns(int index){
		return dataset.get(index);
	}
	
	private void calculateKNeighbors(){
		int size = nodeset.size();
		
		// distance between two nodes
		for(int i=0; i<size; i++){ // for each instance
			double[] lsEach = new double[size];
			for(int j=0; j<size; j++){ // calculate distance from other instance
				lsEach[j] = nodeset.get(i).getDistanceToOther(nodeset.get(j));
			}
			lsDistance.add(lsEach);
		}
		
		// k nearest neighbors for each instance
		for(int i=0; i<lsDistance.size(); i++){
			double kdis = DistanceCalculator.findKDistance(lsDistance.get(i), K);
			HILNode currentInstance = nodeset.get(i);
			for(int j=0; j<lsDistance.size(); j++){
				if(currentInstance.getDistanceToOther(nodeset.get(j)) <= kdis && j != i){
					currentInstance.setNeighbor(nodeset.get(j));
				}	
			}	
			currentInstance.setWeight();
		}		
	}
	
	private void rankingByWeights(){
		nodeset.sort(new WeightComparator());
		int outlierNum = (int)(nodeset.size()*N);
		
		for(int i=0; i<outlierNum; i++){
			nodeset.get(i).setPrelabel("outlier");
		}
		
	}
	
	public void showResults(){
		System.out.println("Experiments Results of <" + dataset.relationName() + "> By HilOut Outlier Detection Method.");
		System.out.println("\n---------------- Detected Outliers ------------------\n");
		for(int i=0; i<nodeset.size(); i++){
			if(nodeset.get(i).isOutlier())
				System.out.println(i+" "+"weight: " + nodeset.get(i).getWeight() + ", Label: " + nodeset.get(i).getLabel());
		}
		System.out.println("\n---------------- Detected Normals ------------------\n");
		for(int i=0; i<nodeset.size(); i++){
			if(!nodeset.get(i).isOutlier())
				System.out.println(i+" "+"weight: " + nodeset.get(i).getWeight() + ", Label: " + nodeset.get(i).getLabel());
		}
		System.out.println("----------------------------------");
		
		MeasureCalculator mc = new MeasureCalculator(nodeset);
		
		System.out.println("TP:" + mc.getTP());
		System.out.println("TN:" + mc.getTN());
		System.out.println("FP:" + mc.getFP());
		System.out.println("FN:" + mc.getFN());
		
//		System.out.println("PRECISION:" + mc.getPRECISION());
//		System.out.println("RECALL:" + mc.getRECALL());
//		System.out.println("F-MEASURE:" + mc.getFMEASURE());
//		System.out.println("ACCURACY:" + mc.getCORRECTRATIO());
		
		System.out.println("Detection Rate: " + mc.getDetectRate());
		System.out.println("FP Rate       : " + mc.getFPRate());

	}
	
	public double getDetectionRate(){
		MeasureCalculator mc = new MeasureCalculator(nodeset);
		return mc.getDetectRate();
	}
	
	public double getFPRate(){
		MeasureCalculator mc = new MeasureCalculator(nodeset);
		return mc.getFPRate();
	}
}


