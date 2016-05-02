/**
 * ADE 1.0 
 * Copyright 1997-2012 HRILab (http://hrilab.org/) 
 * 
 * All rights reserved.  Do not copy and use without permission. 
 * For questions contact Matthias Scheutz at mscheutz@indiana.edu
 *
 * Reference.java
 *
 * @author Paul Schermerhorn
 *
 */
package com;

import java.io.Serializable;

/** Representation of variable logical form. */
public class Reference extends Symbol implements Serializable {
    protected String type;
    private Symbol value;

    public String getType() {
        return type;
    }

    /**
     * Check whether the given object is equal to this one.  Two Symbols are equal 
     * if they're of the same class and their names are the same.
     * @param o the Object to compare this with
     * @return true if they're equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        System.out.println("WARNING: Reference.equals does not check binding...");
        String n = getName();
        // needs to be the same class to be equal
        if (! getClass().equals(o.getClass())) {
            return false;
        }
        // needs to have the same name to be equal
        if (! n.equals(((Reference)o).getName())) {
            return false;
        }
        // needs to have the same type to be equal
        return type.equals(((Reference)o).getType());
    }

    /**
     * Calculate hash code for this Reference.  This is based on the hash codes of
     * the name and type.
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 19 * hash + type.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        String ret = getName()+" - "+getType();
        return ret;
    }

    @Override
    public String toCoreString() {
	String ret;
	if(value != null) ret = value.toCoreString();
	else ret = getName();
	return ret;
    }
    
    public Reference(Variable v) {
        super(v.getName());
        type = v.getType();
    }

    public Reference(String n) {
        super(n);
        type = "entity";
    }

    public Reference(String n, String t) {
        super(n);
        type = t;
    }
}
