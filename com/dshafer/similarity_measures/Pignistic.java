package com.dshafer.similarity_measures;
import com.dshafer.*;
import java.util.List;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Collections;

public class Pignistic extends SimilarityMeasure {

        public double computeSimilarity(DSFrame s1, DSFrame s2){

                double pignistic_coefficient, set2Size, differenceSize;
                Set<String> set2 = new HashSet<String>();
                Set<String> difference = new HashSet<String>();

                set2.addAll(s2.getHypotheses());
                difference.addAll(s1.getHypotheses());
                difference.retainAll(s2.getHypotheses());

                set2Size = (double) set2.size();
                differenceSize = (double) difference.size();

                System.out.println("Size of set2: " + set2Size);
                System.out.println("Size of difference: " + differenceSize);
                pignistic_coefficient = (differenceSize / set2Size);
                System.out.println("pignistic_coefficient : " + pignistic_coefficient);
                return (1.0 - pignistic_coefficient);
        }
}
