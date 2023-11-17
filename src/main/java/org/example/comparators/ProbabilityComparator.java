package org.example.comparators;

import org.example.nodes.GAUNode;

import java.util.Comparator;

// gauss probability comparator
public class ProbabilityComparator implements Comparator<GAUNode> {

    public int compare(GAUNode o1, GAUNode o2) {
        if (o1.getProbability() > o2.getProbability()) {
            return 1;
        } else if (o1.getProbability() < o2.getProbability()) {
            return -1;
        } else {
            return 0;
        }
    }
}
