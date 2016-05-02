package com.dshafer.similarity_measures;
import com.dshafer.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.BitSet;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Collections;
import java.util.Iterator;
import java.util.BitSet;

public class Confusion extends SimilarityMeasure {

        public double computeSimilarity(DSFrame s1, DSFrame s2) {

            Map<BitSet,Double> setA = s1.getMasses();
            Map<BitSet,Double> setB = s2.getMasses();

            double confusion_dissimilarity = 0.0;
            double sumA = 0.0, sumB = 0.0;

            for(BitSet key : setA.keySet()){
                sumA += setA.get(key) * summation_log2(s1.getBel(key));
                System.out.println("s1.getBel(key) : " + s1.getBel(key));
                System.out.println("Log : " + Math.log(s1.getBel(key)));
                System.out.println("sumA : " + sumA);
            }

            for(BitSet key: setB.keySet()){
                sumB += setB.get(key) * summation_log2(s2.getBel(key));
                System.out.println("s2.getBel(key) : " + s2.getBel(key));
                System.out.println("Log : " + Math.log(s2.getBel(key)));
                System.out.println("sumB : " + sumB);
            }

            confusion_dissimilarity = Math.abs(sumA - sumB);
            System.out.println("confusion_dissimilarity : " + confusion_dissimilarity);

            return confusion_dissimilarity;
        }

        public double summation_log2(double n){
            if(n == 0){
                return 0;
            } else {
                return (Math.log(n)/ Math.log(2));
            }
        }

}
