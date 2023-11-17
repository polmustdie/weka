package org.example.utils;

import java.util.HashSet;
import java.util.Set;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ARFFReader {

	private Instances dataset;

	private double[] mu;
	private double[] std;
	private Set<String> labels = new HashSet<>();

	public ARFFReader(String path){
		try {

			dataset = DataSource.read(path);
			dataset.setClassIndex(dataset.numAttributes()-1);
			
			// attribute-value normalization operation

//			Normalize nm = new Normalize();
//			nm.setInputFormat(dataset);
//			dataset = Filter.useFilter(dataset, nm);

		} catch (Exception e) {
			System.out.println("Loading files error!");
			e.printStackTrace();
		}

		for(Instance ins: dataset){
			String label = ins.stringValue(dataset.classAttribute());
			labels.add(label);
		}
		
		//calculating mu std
		try{
			setMu();
			setStd();
		}catch(Exception e){
			System.out.println("Calculating parameters error!");
			e.printStackTrace();
		}
		
	}
	
	//for DBSCAN
	public ARFFReader(String path, boolean flag){
		try {
			dataset = DataSource.read(path);

//			Normalize nm = new Normalize();
//			nm.setInputFormat(dataset);
//			dataset = Filter.useFilter(dataset, nm);

		} catch (Exception e) {
			System.out.println("Loading files error!");
			e.printStackTrace();
		}
//		for(Instance ins: dataset){
//			String label = ins.stringValue(dataset.classAttribute());
//			labels.add(label);
//		}
		
		try{
			setMu();
			setStd();
		}catch(Exception e){
			System.out.println("Calculating parameters error!");
			e.printStackTrace();
		}
		
	}
	
	public Instances getDataset(){
		return dataset;
	}
	
	public void setMu(){
		int attrNum = this.dataset.numAttributes();
		int insNum = this.dataset.numInstances();
		double[] mus = new double[attrNum - 1];
		
		for(int i=0; i<attrNum-1; i++){
			double sum = 0.0d;
			for(int j=0; j <insNum; j++){
				sum += dataset.get(j).value(i);
			}
			mus[i] = (sum*1.0)/(insNum*1.0);
		}
		
		this.mu = mus;
	}
	
	public double[] getMu(){
		return this.mu;
	}
	
	// std for each attribute
	public void setStd(){
		int attrNum = this.dataset.numAttributes();
		int insNum = this.dataset.numInstances();
		double[] stds = new double[attrNum - 1];
		
		for(int i=0; i<attrNum-1; i++){
			double delt = 0.0d;
			for(int j=0; j <insNum; j++){
				delt += Math.pow((dataset.get(j).value(i) - mu[i]), 2);
			}
			stds[i] = Math.sqrt((delt*1.0)/((insNum-1)*1.0));
		}
		
		this.std = stds;
		
	}
	
	public double[] getStd(){
		return this.std;
	}
	
	public void showDataset(){
		System.out.println("----------   Dataset Basic Information   ----------");
		System.out.println("(1) Relation Name : " + dataset.relationName());
		System.out.println("(2) Instances     : " + dataset.numInstances());
		System.out.println("(3) Attributes    : " + (dataset.numAttributes()-1));
		System.out.println("(4) Class Labels  : " + labels);
	}

}
