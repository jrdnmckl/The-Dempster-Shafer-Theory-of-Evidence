package com.dshafer;

import java.util.Comparator;
import java.util.BitSet;

public class CardinalityComparator implements Comparator<BitSet> {

	@Override
	public int compare(BitSet b1, BitSet b2) {
		if(b1.cardinality() < b2.cardinality()) return -1;
		else if(b1.cardinality() > b2.cardinality()) return 1;
		return 0;
	}

	
	
}
