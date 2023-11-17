package org.example.nodes;

import org.example.HilOut;
import org.example.utils.DistanceCalculator;
import weka.core.Instance;

import java.util.ArrayList;
import java.util.List;


public class HILNode extends WekaNode {

    private List<HILNode> KNeighbors = new ArrayList<HILNode>(); // k-nearest neighbors

    private String label; // class label

    private String prelabel = "normal"; // outlier or normal

    private List<Double> lsAttr = new ArrayList<Double>(); // feature list

    private double weight = 0.0d; // weight value


    public HILNode(Instance instance) {
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

    public double getDistanceToOther(HILNode node) {
        double distance = 0.0d;
        List<Double> attr1 = lsAttr;
        List<Double> attr2 = node.getAttr();

        distance = DistanceCalculator.distanceEculidean(attr1, attr2); //List<Double> ls1, List<Double> ls2

        return distance;
    }


    public void setNeighbor(HILNode node) {
        if (KNeighbors.size() < HilOut.K)
            KNeighbors.add(node);
    }


    public void setWeight() {
        for (HILNode nodes : KNeighbors) {
            weight += getDistanceToOther(nodes);
        }
    }


    public double getWeight() {
        return weight;
    }


    public void showNode() {
        System.out.println("Instance Details: [" + label + "]\n---------------------");
        for (double feature : lsAttr) {
            System.out.print(feature + ", ");
        }
        System.out.println("");
    }
}
