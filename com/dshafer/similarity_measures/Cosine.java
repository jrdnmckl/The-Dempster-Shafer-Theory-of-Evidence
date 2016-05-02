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

public class Cosine extends SimilarityMeasure {

        public double computeSimilarity(DSFrame s1, DSFrame s2) {

            Map<BitSet,Double> setA = s1.getMasses();
            Map<BitSet,Double> setB = s2.getMasses();
            
            System.out.println("Map of set A: ");
            System.out.println(setA);
            System.out.println("Values of set A: ");
            System.out.println(setA.values());
            System.out.println("Map of set B: ");
            System.out.println(setB);
            System.out.println("Values of set B: ");
            System.out.println(setB.values());

            double[] vector = new double[setA.values().size()];
            double[] vector2 = new double[setB.values().size()];
            int index = 0;

            System.out.println("vector 1 values : ");

            //create vector for masses
            for (BitSet key : setA.keySet()){
                vector[index] = setA.get(key);
                System.out.println(vector[index]);
                System.out.println("index at : " + index);
                index++;
            }
            
            //reset index for second vector
            index = 0;

            for (BitSet key : setB.keySet()){
                vector2[index] = setB.get(key);
                System.out.println(vector[index]);
                System.out.println("index at : " + index);
                index++;
            }

            double dotProduct = 0.0, magnitudeA = 0.0, magnitudeB = 0.0;
            double cosine_similarity = 0.0;

            // sum up dotProduct, and magnitudes for both vectors
            for(int i = 0; i < vector.length; i++){
                dotProduct += (vector[i] * vector2[i]);
                magnitudeA += (vector[i] * vector[i]);
                magnitudeB += (vector2[i] * vector2[i]);
            }
            magnitudeA = Math.sqrt(magnitudeA);
            magnitudeB = Math.sqrt(magnitudeB);
            System.out.println("Final dotProduct : " + dotProduct);
            System.out.println("Final A Mag : " + magnitudeA);
            System.out.println("Final B Mag : " + magnitudeB);

            cosine_similarity = dotProduct / (magnitudeA * magnitudeB);
            System.out.println("cosine_similarity : " + cosine_similarity);
            return cosine_similarity;
        }


}
