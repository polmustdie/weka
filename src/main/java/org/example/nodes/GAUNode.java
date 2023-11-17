package org.example.nodes;

import weka.core.Instance;

import java.util.ArrayList;
import java.util.List;


public class GAUNode extends WekaNode {

    private String label; // class label

    private String prelabel = "normal"; // outlier or normal

    private List<Double> lsAttr = new ArrayList<Double>(); // feature list

    private double probability = 0.0d; // weight value


    public GAUNode(Instance instance) {
        int lenAttr = instance.numAttributes();
        label = instance.stringValue(lenAttr - 1); // set true label
        for (int i = 0; i < lenAttr - 1; i++) { // set feature-values
            lsAttr.add(instance.value(i));
        }
    }

    public List<Double> getAttr() {
        return lsAttr;
    }


    public void setPrelabel(String flag) {
        this.prelabel = flag;
    }

    @Override
    public String getLabel() {
        return label;
    }


    @Override
    public boolean isOutlier() {
        if (prelabel == "outlier") {
            return true;
        } else {
            return false;
        }
    }

    public void setProbability(double p) {
        this.probability = p;
    }

    public double getProbability() {
        return this.probability;
    }
}
