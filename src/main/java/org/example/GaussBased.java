package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.example.comparators.ProbabilityComparator;
import org.example.nodes.GAUNode;
import org.example.utils.ARFFReader;
import org.example.utils.MeasureCalculator;
import weka.core.Instance;
import weka.core.Instances;

/*

  It assumes that the instance distribution is under the Gauss Distribution, and the instances with
  low probability is detected as the outliers.

  1. Get mean value <b>&mu;</b> of each dimension (feature).
  2. Get the standard deviation sigma of each dimension (feature).
  3. Get p(x) of each instance x, and select the top-N< instances who
  has the smallest p(x), they are detected as an outlier. Note that p(x) is defined as follows,

  p(x) = 1/(sqrt(2&pi;)*sigma;j) * exp((-1)*(xj-&mu;j)^2/(2*&sigma;^2))
 */
public class GaussBased {
	
	private Instances dataset;
	//top n outliers
	public static final double N = 0.1;
//	public static double threshold = 1.0E-07;
	
	private static List<GAUNode> nodeset = new ArrayList<>();
	
	public GaussBased(String path){
		
		nodeset.clear();
		
		ARFFReader reader = new ARFFReader(path);
		dataset = reader.getDataset();
		for(int i=0; i<dataset.numInstances(); i++){
			Instance currentInstance = dataset.get(i);
			GAUNode node = new GAUNode(currentInstance);
			nodeset.add(node);
		}
		
		calculateProbability(path);
		
		rankingByProbability();
		
//		for(int i=0; i<nodeset.size(); i++){
//			System.out.println(i + ">>" + nodeset.get(i).getProbability());
//		}

//		reader.showDataset();
	}
	
	// probability of each node
	private void calculateProbability(String path){

		ARFFReader reader = new ARFFReader(path);
		double[] mus = reader.getMu();
		double[] stds = reader.getStd();

		for(int i=0; i<nodeset.size(); i++){
			List<Double> lsAttr = nodeset.get(i).getAttr();
			double pi = 1.0d;
			
			for(int j=0; j<dataset.numAttributes()-1; j++){
				double xJ = lsAttr.get(j);
				double currentMu = mus[j];
				double currentStd = stds[j];
				if(currentStd-0 == 0){ // if some dimension's std is equal to 0.
					continue;
				}
				pi *= (1.0/(Math.sqrt(2.0*Math.PI)*currentStd)) * Math.exp((-1.0)*(xJ - currentMu)*(xJ - currentMu)/(2*currentStd*currentStd));
			}
			nodeset.get(i).setProbability(pi);
		}
	}
//	public double selectThreshold(){
//		double bestEpsilon = 0;
//		double f = 0;
//		double maxProb = nodeset.stream().max(new ProbabilityComparator()).get().getProbability();
//		double minProb = nodeset.stream().min(new ProbabilityComparator()).get().getProbability();
//		double stepsize = (maxProb-minProb)/1000;
//		ArrayList<Double> epsilons = new ArrayList<>();
//		while (minProb < maxProb){
//			minProb = minProb + stepsize;
//			epsilons.add(minProb);
//		}
//		for (int j = 0; j<epsilons.size(); j++) {
//			for (int i = 0; i < nodeset.size(); i++) {
//				if (nodeset.get(i).getProbability()<epsilons.get(j)){
//
//				}
//			}
//		}
//
//
//	}
//	def select_threshold(probs, test_data):
//	best_epsilon = 0
//	best_f1 = 0
//	f = 0
//	stepsize = (max(probs) - min(probs)) / 1000;
//	epsilons = np.arange(min(probs), max(probs), stepsize)
//			for epsilon in np.nditer(epsilons):
//	predictions = (probs < epsilon)
//	f = f1_score(test_data, predictions, average='binary')
//        if f > best_f1:
//	best_f1 = f
//			best_epsilon = epsilon
//
//    return best_f1, best_epsilon
	private void rankingByProbability(){
		Collections.sort(nodeset, new ProbabilityComparator());
		int topNum = (int)(N*nodeset.size());
//		double mean = 0;
//		for (int i = 0; i<nodeset.size(); i++){
//			mean = mean + nodeset.get(i).getProbability();
////			if (nodeset.get(i).getProbability() < threshold){
//////				nodeset.get(i).setPrelabel("outlier");
////			}
//
		for(int i=0; i<topNum; i++){
			nodeset.get(i).setPrelabel("outlier");
		}
//		mean = mean /nodeset.size();
//		for (int i =0; i<nodeset.size();i++){
//			if (nodeset.get(i).getProbability() < mean){
//				nodeset.get(i).setPrelabel("outlier");
//		}
//	}
//		System.out.println("MEAN " + mean);
	}
	
	public void showResults(){
		System.out.println("Experiments Results of <" + dataset.relationName() + "> By Using Gauss-Based Outlier Detection Method.");
		System.out.println("\n---------------- Detected Outliers ------------------\n");
		for(int i=0; i<nodeset.size(); i++){
			if(nodeset.get(i).isOutlier())
				System.out.println("probability: " + nodeset.get(i).getProbability() + ", Label: " + nodeset.get(i).getLabel());
		}
		System.out.println("\n---------------- Detected Normals ------------------\n");
		for(int i=0; i<nodeset.size(); i++){
			if(!nodeset.get(i).isOutlier())
				System.out.println("probability: " + nodeset.get(i).getProbability() + ", Label: " + nodeset.get(i).getLabel());
		}
		System.out.println("----------------------------------");
		
		MeasureCalculator mc = new MeasureCalculator(nodeset);
		
		System.out.println("TP: " + mc.getTP());
		System.out.println("TN: " + mc.getTN());
		System.out.println("FP: " + mc.getFP());
		System.out.println("FN: " + mc.getFN());
		
//		System.out.println("PRECISION: " + mc.getPRECISION());
//		System.out.println("RECALL: " + mc.getRECALL());
		System.out.println("F-MEASURE: " + mc.getFMEASURE());
//		System.out.println("ACCURACY: " + mc.getCORRECTRATIO());
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

