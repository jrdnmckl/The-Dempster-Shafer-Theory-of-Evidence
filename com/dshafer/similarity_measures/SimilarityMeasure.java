package com.dshafer.similarity_measures;
import java.util.List;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Collections;
import com.dshafer.*;

public abstract class SimilarityMeasure {

        public abstract double computeSimilarity(DSFrame s1, DSFrame s2);
}
