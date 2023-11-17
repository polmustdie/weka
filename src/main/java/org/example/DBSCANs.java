package org.example;

import java.util.ArrayList;
import java.util.List;

import org.example.nodes.SBSNode;
import org.example.utils.ARFFReader;
import org.example.utils.MeasureCalculator;
import weka.core.Instance;
import weka.core.Instances;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.DBSCAN;

// dbscan - clustering algo
public class DBSCANs {
	private Instances dataset;
	// Epsilon in DBSCAN>??????????????????
	public static final double EPS = 0.2;

	public static final int MIN_POINTS = 5;
	
	private static List<SBSNode> nodeset = new ArrayList<>();
	
	public DBSCANs(String path){
		
		nodeset.clear();
		
		ARFFReader reader = new ARFFReader(path, true);
		dataset = reader.getDataset();
//		dataset.deleteAttributeAt(dataset.numAttributes()-1); //DBSCAN is a unsuperviesd method.
		for(int i=0; i<dataset.numInstances(); i++){
			Instance currentInstance = dataset.get(i);
			SBSNode node = new SBSNode(currentInstance);
			nodeset.add(node);
		}
		
		try {
			clusteringByDBSCAN(path);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	// probability of each node
	private void clusteringByDBSCAN(String path) throws Exception{
		DBSCAN dbscan = new DBSCAN();
		dbscan.setEpsilon(EPS);
		dbscan.setMinPoints(MIN_POINTS);
		
		dbscan.buildClusterer(dataset);
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(dbscan);
		eval.evaluateClusterer(dataset);
		double[] num = eval.getClusterAssignments();
//		System.out.println(num.length);
		
		for(int i=0; i<nodeset.size(); i++){
			SBSNode currentNode = nodeset.get(i);
			currentNode.setClusterIndex(num[i]);
			if(num[i] < 0){
				currentNode.setPrelabel("outlier");
			}
		}
		
	}
	public void showResults(){
		System.out.println("\nExperiments Results of <" + dataset.relationName() + "> By Using DBSCAN Outlier Detection Method.");
		System.out.println("\n---------------- Detected Outliers ------------------\n");
		for(int i=0; i<nodeset.size(); i++){
			if(nodeset.get(i).isOutlier())
				System.out.println(i + " " + nodeset.get(i).getClusterIndex() + ", Label: " + nodeset.get(i).getLabel());
		}
		System.out.println("\n---------------- Detected Normals ------------------\n");
		for(int i=0; i<nodeset.size(); i++){
			if(!nodeset.get(i).isOutlier())
				System.out.println(i + " " +nodeset.get(i).getClusterIndex() + ", Label: " + nodeset.get(i).getLabel());
		}
		System.out.println("----------------------------------");
		
		MeasureCalculator mc = new MeasureCalculator(nodeset);
		
		System.out.println("TP:" + mc.getTP());
		System.out.println("TN:" + mc.getTN());
		System.out.println("FP:" + mc.getFP());
		System.out.println("FN:" + mc.getFN());
//		
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

