/**
 * ADE 1.0 
 * Copyright 1997-2010 HRILab (http://hrilab.org/) 
 * 
 * All rights reserved.  Do not copy and use without permission. 
 * For questions contact Matthias Scheutz at mscheutz@indiana.edu
 *
 * Predicate.java
 *
 * @author Paul Schermerhorn
 *
 */
package com;

import java.io.Serializable;
import java.util.*;

/** Representation of predicate logical form. */
public class Predicate extends Term implements Serializable {

    // not really much difference from Term, for our purposes
    public Predicate(String n) {
        super(n);
    }

    public Predicate(Term t) {
        super(t);
    }

    public Predicate(Predicate p) {
        super((Term)p);
    }

    public Predicate(String n, ArrayList<Symbol> a) {
        super(n, a);
    }

    public Predicate(String n, Symbol... a) {
        super(n, a);
    }

    public Predicate(String n, String... a) {
        super(n, a);
    }

    @Override
    public Predicate copy() {
	return new Predicate(this);
    }
}
