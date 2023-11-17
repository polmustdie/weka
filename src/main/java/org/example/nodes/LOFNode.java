package org.example.nodes;

import weka.core.Instance;

import java.util.ArrayList;
import java.util.List;

/***
 * <p>This class <b>LOFNode</b> is used to simulate the characteristic of each instance.</p>
 *
 */
public class LOFNode extends WekaNode {

    private String label; // class label

    private String prelabel = "normal"; // outlier or normal

    private List<Double> lsAttr = new ArrayList<Double>(); // feature list

    private double lof = 0.0d; // weight value

    /**
     * To initialize the instance with features and class label
     */
    public LOFNode(Instance instance) {
        int lenAttr = instance.numAttributes();
        label = instance.stringValue(lenAttr - 1); // set true label
        for (int i = 0; i < lenAttr - 1; i++) { // set feature-values
            lsAttr.add(instance.value(i));
        }
    }

    /**
     * <p>To get <b>feature-values</b> of instance.
     */
    public List<Double> getAttr() {
        return lsAttr;
    }

    /**
     * To save predicted flag, i.e., '<b>normal</b>' or '<b>outlier</b>'.
     */
    public void setPrelabel(String flag) {
        this.prelabel = flag;
    }

    /**
     * To get the original class label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * To judge whether the instance is predicted as a outlier.
     */
    public boolean isOutlier() {
        if (prelabel == "outlier") {
            return true;
        } else {
            return false;
        }
    }

    public void setLOF(double lof) {
        this.lof = lof;
    }

    public double getLOF() {
        return this.lof;
    }
}
