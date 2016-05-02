package com.dshafer.similarity_measures;
import com.dshafer.*;
import java.util.List;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Collections;

public class Diaz extends SimilarityMeasure {

        public double computeSimilarity(DSFrame s1, DSFrame s2){

                double diaz_factor, sizeA, sizeB, interSize;
                Set<String> setA = new HashSet<String>();
                Set<String> setB = new HashSet<String>();
                Set<String> inter = new HashSet<String>();

                setA.addAll(s1.getHypotheses());
                setB.addAll(s2.getHypotheses());
                inter.addAll(s1.getHypotheses());
                inter.retainAll(s2.getHypotheses());

                sizeA = (double) setA.size();
                sizeB = (double) setB.size();
                interSize = (double) inter.size();

                System.out.println("Size of setA: " + sizeA);
                System.out.println("Size of setB: " + sizeB);
                diaz_factor = ((2 * interSize) / (sizeA + sizeB));
                System.out.println("diaz_factor : " + diaz_factor);
                return diaz_factor;
        }
}
