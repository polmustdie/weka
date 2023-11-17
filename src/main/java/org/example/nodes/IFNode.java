package org.example.nodes;

import weka.core.Instance;

import java.util.ArrayList;
import java.util.List;


public class IFNode extends WekaNode {

    private String label; // class label

    private String prelabel = "normal"; // outlier or normal

    private List<Double> lsAttr = new ArrayList<Double>(); // feature list

    private double score = 0.0d; // cluster index


    public IFNode(Instance instance) {
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


    public String getLabel() {
        return label;
    }

    public boolean isOutlier() {
        if (prelabel == "outlier") {
            return true;
        } else {
            return false;
        }
    }

    public void setScore(double s) {
        this.score = s;
    }

    public double getScore() {
        return this.score;
    }
}
