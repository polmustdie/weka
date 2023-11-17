package org.example.comparators;

import org.example.nodes.HILNode;

import java.util.Comparator;

// hilout comparator to choose top n outliers
public class WeightComparator implements Comparator<HILNode> {

    public int compare(HILNode o1, HILNode o2) {
        if (o1.getWeight() > o2.getWeight()) {
            return -1;
        } else if (o1.getWeight() < o2.getWeight()) {
            return 1;
        } else {
            return 0;
        }

    }

}
