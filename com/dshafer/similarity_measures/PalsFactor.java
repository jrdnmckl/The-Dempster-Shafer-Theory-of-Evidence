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

public class PalsFactor extends SimilarityMeasure {

        public double computeSimilarity(DSFrame s1, DSFrame s2) {

            Map<BitSet,Double> setA = s1.getMasses();
            Map<BitSet,Double> setB = s2.getMasses();

            double pals_factor = 0.0;
            double sumA = 0.0, sumB = 0.0;

            for(BitSet key : setA.keySet()){
                sumA += setA.get(key) * summation_log2(setA.get(key));
                System.out.println("setA.get(key) : " + setA.get(key));
                System.out.println("Log : " + Math.log(setA.get(key)));
                System.out.println("sumA : " + sumA);
            }

            for(BitSet key: setB.keySet()){
                sumB += setB.get(key) * summation_log2(setB.get(key));
                System.out.println("setB.get(key) : " + setB.get(key));
                System.out.println("Log : " + Math.log(setB.get(key)));
                System.out.println("sumB : " + sumB);
            }

            pals_factor = Math.abs(sumA - sumB);
            System.out.println("pals_factor : " + pals_factor);

            return pals_factor;
        }

        public double summation_log2(double n){
            if(n == 0){
                return 0;
            } else {
                return (Math.log(n)/ Math.log(2));
            }
        }

}
