/**
 * ADE 1.0 
 * Copyright 1997-2010 HRILab (http://hrilab.org/) 
 * 
 * All rights reserved.  Do not copy and use without permission. 
 * For questions contact Matthias Scheutz at mscheutz@indiana.edu
 *
 * Variable.java
 *
 * @author Paul Schermerhorn
 *
 */
package com;

import java.io.Serializable;

/** Representation of variable logical form. */
public class Variable extends Symbol implements Serializable {
    private String type;
    private Symbol value = null;

    public String getType() {
        return type;
    }

    // PWS: not sure this makes sense
    public void setType(String t) {
        type = t;
    }

    public Symbol getValue() {
	return value;
    }

    public void setValue(Symbol s) {
	value = s;
    }

    /**
     * Check whether the given object is equal to this one.  Two Symbols are equal 
     * if they're of the same class and their names are the same.
     * Should also be equal if they're bound to the same thing.
     * @param o the Object to compare this with
     * @return true if they're equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        String n = getName();
        // needs to be the same class to be equal
        if (! getClass().equals(o.getClass())) {
            return false;
        }
        // needs to have the same name to be equal
        if (! n.equals(((Variable)o).getName())) {
            return false;
        }
        // needs to have the same type to be equal
        return type.equals(((Variable)o).getType());
    }

    /**
     * Calculate hash code for this Variable.  This is based on the hash codes of
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
	else ret = getType();
	return ret;
    }
    
    @Override
    public Object clone() {
        Variable cloned = new Variable(getName(), type);
        cloned.setValue(value);
        
        return cloned;
    }

    public Variable(Variable v) {
        super(v.getName());
        type = v.getType();
    }

    public Variable(String n) {
        super(n);
        type = "entity";
    }

    public Variable(String n, String t) {
        super(n);
        type = t;
    }
}
