package org.example.utils;

import java.util.List;
import org.example.nodes.WekaNode;

public class MeasureCalculator{
	
	private List<? extends WekaNode> lsNode;
	
	private int tp = 0;
	
	private int tn = 0;
	
	private int fp = 0;
	
	private int fn = 0;
	
	private double precision = 0.0d;
	
	private double recall = 0.0d;
	
	private double fmeasure = 0.0d;
	
	private double correctratio = 0.0d;
	
	public MeasureCalculator(List<? extends WekaNode> ls){
		this.lsNode = ls;
		
		initialize();
	}
	
	private void initialize(){
		setTP();
		setTN();
		setFP();
		setFN();
		setPRECISION();
		setRECALL();
		setFMEASURE();
		setCORRECTRATIO();
	}
	
	private void setTP(){
		int count = 0;
		for(int i=0; i<lsNode.size(); i++){
			WekaNode currentNode = lsNode.get(i);
			if(currentNode.getLabel().equals("no") &&
					currentNode.isOutlier()){
				count++;
			}
		}
		tp = count;
	}

	private void setTN(){
		int count = 0;
		for(int i=0; i<lsNode.size(); i++){
			WekaNode currentNode = lsNode.get(i);
			if(currentNode.getLabel().equals("ok") &&
					!currentNode.isOutlier()){
				count++;
			}
		}
		tn = count;
	}

	private void setFP(){
		int count = 0;
		for(int i=0; i<lsNode.size(); i++){
			WekaNode currentNode = lsNode.get(i);
			if(currentNode.getLabel().equals("ok") &&
					currentNode.isOutlier()){
				count++;
			}
		}
		fp = count;
	}

	private void setFN(){
		int count = 0;
		for(int i=0; i<lsNode.size(); i++){
			WekaNode currentNode = lsNode.get(i);
			if(currentNode.getLabel().equals("no") &&
					!currentNode.isOutlier()){
				count++;
			}
		}
		fn = count;
	}
	
	private void setPRECISION(){
		precision = tp*1.0/(tp+fp)*1.0;
	}
	
	private void setRECALL(){
		recall = tp*1.0/(tp+fn)*1.0;
	}
	
	private void setFMEASURE(){
		fmeasure = 2.0*precision*recall/(precision + recall)*1.0;
	}
	
	private void setCORRECTRATIO(){
		correctratio = (tp + tn)*1.0/(tp + fp + tn + fn)*1.0;
	}
	
	/** To get FP from Confusion Matrix*/
	public int getFN(){
		return this.fn;
	}
	
	/** To get FP from Confusion Matrix*/
	public int getFP(){
		return this.fp;
	}
	
	/** To get TP from Confusion Matrix*/
	public int getTP(){
		return this.tp;
	}
	
	/** To get TN from Confusion Matrix*/
	public int getTN(){
		return this.tn;
	}
	
	/** To get precision from Confusion Matrix*/
	public double getPRECISION(){
		return this.precision;
	}
	
	/** To get recall from Confusion Matrix*/
	public double getRECALL(){
		return this.recall;
	} 
	
	/** To get f-measure from Confusion Matrix*/
	public double getFMEASURE(){
		return this.fmeasure;
	}
	
	/** To get correct ratio from Confusion Matrix*/
	public double getCORRECTRATIO(){
		return this.correctratio;
	}
	
	/** To get detection rate form Confusion Matrix*/
	public double getDetectRate(){
		return getTP()*1.0/(getTP()+getFN())*1.0;
	}
	
	/** To get FP rate form Confusion Matrix*/
	public double getFPRate(){
		return getFP()*1.0/(getFP()+getTN())*1.0;
	}

}
