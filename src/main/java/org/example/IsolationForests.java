package org.example;

import java.util.ArrayList;
import java.util.List;

import org.example.nodes.IFNode;
import org.example.utils.ARFFReader;
import org.example.utils.MeasureCalculator;
import weka.classifiers.misc.IsolationForest;
import weka.core.Instance;
import weka.core.Instances;

/*
  each data point will be assigned an anomaly score s in the range between 0 and 1.
  If the s in (0.5, 1], then this point have high probability to be detected as an outlier;
  else if the s in [0, 0.5), then this point can be regarded as an normal.

  1. Randomly selecting some subsets of dataset.
  2. Using these subsets to construct iTrees by randomly split a randomly selected feature.
  3. The nodes(instances) that have the longer average path length are detected as outliers.
 */
public class IsolationForests {
	
	private static Instances dataset;
	//number of instances in itree
	public static final int INSTANCES_NUMBER = 30;
	// number of trees
	public static final int TREES_NUMBER = 15;
	// threshold
	public static final double S_THRESHOLD = 0.5;
	
	private static List<IFNode> nodes = new ArrayList<IFNode>();
	
	public IsolationForests(String path){
		
		nodes.clear();
		
		ARFFReader reader = new ARFFReader(path);
		dataset = reader.getDataset();
		
		for(int i=0; i<dataset.numInstances(); i++){
			Instance currentInstance = dataset.get(i);
			IFNode node = new IFNode(currentInstance);
			nodes.add(node);
		}
		
		try {
			setAnomalyScore();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}	
	
	public void setAnomalyScore() throws Exception{
		
		IsolationForest iforest = new IsolationForest();
		iforest.setNumTrees(TREES_NUMBER);
		iforest.setSubsampleSize(INSTANCES_NUMBER);
		iforest.buildClassifier(dataset);
		
		for(int i = 0; i< nodes.size(); i++){
			double score = iforest.distributionForInstance(dataset.get(i))[1];
			nodes.get(i).setScore(score);
			if(score >= S_THRESHOLD){
				nodes.get(i).setPrelabel("outlier");
			}
		}
		
	}

	public void showResults(){
		
		System.out.println("\nExperiments Results of <" + dataset.relationName() + "> By Using Isolation Forest Outlier Detection Method.");
		System.out.println("\n---------------- Detected Outliers ------------------\n");
		for(int i = 0; i< nodes.size(); i++){
			if(nodes.get(i).isOutlier()){
				System.out.println(i + " " + nodes.get(i).getScore() + ", Label: " + nodes.get(i).getLabel());
			}
		}
		System.out.println("\n---------------- Detected Normals ------------------\n");
		for(int i = 0; i< nodes.size(); i++){
			if(!nodes.get(i).isOutlier()){
				System.out.println(i + " " + nodes.get(i).getScore() + ", Label: " + nodes.get(i).getLabel());
			}
		}
		System.out.println("----------------------------------");

		MeasureCalculator mc = new MeasureCalculator(nodes);
		
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
		MeasureCalculator mc = new MeasureCalculator(nodes);
		return mc.getDetectRate();
	}
	
	public double getFPRate(){
		MeasureCalculator mc = new MeasureCalculator(nodes);
		return mc.getFPRate();
	}
	
	
}

