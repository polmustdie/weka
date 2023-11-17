package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.example.comparators.LOFComparator;
import org.example.nodes.LOFNode;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.LOF;
import org.example.utils.ARFFReader;
import org.example.utils.MeasureCalculator;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;

/*
 1. Get k-distance of each instance.
 2. Get K-nearest neighbors of each instance.
 3. Get reach-distance of any pair of instances.
 4. Get local reach-ability density of each instance.
 5. Get LOF factor of each instance, the top-N instances with high LOF are detected as outliers.
 */
public class LocalOutlierFactor {
	private Instances dataset;
	ArrayList<LOFNode> values = new ArrayList<>();
	// n outliers
	public static final double N = 0.15;
	// lower bound
	public static final String LOWER_BOUND = "20";
	// upper bound
	public static final String UPPER_BOUND = "30";
	
	private static List<LOFNode> nodes = new ArrayList<>();
	
	public LocalOutlierFactor(String path){
		
		nodes.clear();
		
		ARFFReader reader = new ARFFReader(path);
		dataset = reader.getDataset();
		for(int i=0; i<dataset.numInstances(); i++){
			Instance currentInstance = dataset.get(i);
			LOFNode node = new LOFNode(currentInstance);
			nodes.add(node);
		}
		
		try {
			calculateLOF();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		rankingByLOF();
		
	}

	private void calculateLOF() throws Exception{

		LOF lof = new LOF();
		lof.setMinPointsLowerBound(LOWER_BOUND);
//		lof.setMinPointsUpperBound(String.valueOf(nodeset.size() - 2));
		lof.setMinPointsUpperBound(UPPER_BOUND);
//		lof.setNNSearch();
		LinearNNSearch searcher = new LinearNNSearch();
//		searcher.setDistanceFunction(new ManhattanDistance());
		searcher.setDistanceFunction(new EuclideanDistance());
		lof.setNNSearch(searcher);
		lof.setInputFormat(dataset);
		dataset = Filter.useFilter(dataset, lof);
		
		for(int i = 0; i< nodes.size(); i++){
			
			nodes.get(i).setLOF(dataset.get(i).value(dataset.numAttributes()-1));
			
		}
	}
	
	private void rankingByLOF(){
		Collections.sort(nodes, new LOFComparator());
		int topNum = (int)(N * nodes.size());
		
		for(int i=0; i<topNum; i++){
			nodes.get(i).setPrelabel("outlier");
			values.add(nodes.get(i));
		}
	}
	
	public void showResults(){
		System.out.println("\nExperiments Results of <" + dataset.relationName() + "> By Using LOF Outlier Detection Method.");
		System.out.println("\n---------------- Detected Outliers ------------------\n");
		for(int i = 0; i< nodes.size(); i++){
			if(nodes.get(i).isOutlier())
				System.out.println("lof: " + nodes.get(i).getLOF() + ", Label: " + nodes.get(i).getLabel());
		}
		System.out.println("\n---------------- Detected Normals ------------------\n");
		for(int i = 0; i< nodes.size(); i++){
			if(!nodes.get(i).isOutlier())
				System.out.println("lof: " + nodes.get(i).getLOF() + ", Label: " + nodes.get(i).getLabel());
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

