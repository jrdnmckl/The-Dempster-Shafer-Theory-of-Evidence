/**
 * ADE 1.0 
 * Copyright 1997-2010 HRILab (http://hrilab.org/) 
 * 
 * All rights reserved.  Do not copy and use without permission. 
 * For questions contact Matthias Scheutz at mscheutz@indiana.edu
 *
 * Symbol.java
 *
 * @author Paul Schermerhorn
 *
 */
package com;

import java.io.Serializable;

/** Representation of pair. */
public class Pair<A, D> implements Serializable {

    protected A car;
    protected D cdr;

    @Override
    public String toString() {
        return car.toString()+" . "+cdr.toString();
    }

    public Pair(A a, D d) {
        car = a;
        cdr = d;
    }

    public A car() {
	return car;
    }

    public D cdr() {
	return cdr;
    }
}
