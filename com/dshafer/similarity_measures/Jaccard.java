package com.dshafer.similarity_measures;
import com.dshafer.*;
import java.util.List;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Collections;

public class Jaccard extends SimilarityMeasure {

        public double computeSimilarity(DSFrame s1, DSFrame s2){

                double jaccard_coefficient, unionSize, differenceSize;
                Set<String> union = new HashSet<String>();
                Set<String> difference = new HashSet<String>();

                union.addAll(s1.getHypotheses());
                union.addAll(s2.getHypotheses());
                difference.addAll(s1.getHypotheses());
                difference.retainAll(s2.getHypotheses());

                unionSize = (double) union.size();
                differenceSize = (double) difference.size();

                System.out.println("Size of union: " + unionSize);
                System.out.println("Size of difference: " + differenceSize);
                jaccard_coefficient = (differenceSize / unionSize);
                System.out.println("jaccard_coefficient : " + jaccard_coefficient);
                return (1.0 - jaccard_coefficient);
        }
}
