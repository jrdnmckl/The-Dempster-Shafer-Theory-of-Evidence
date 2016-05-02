/**
 * ADE 1.0 
 * Copyright 1997-2010 HRILab (http://hrilab.org/) 
 * 
 * All rights reserved.  Do not copy and use without permission. 
 * For questions contact Matthias Scheutz at mscheutz@indiana.edu
 *
 * Constant.java
 *
 * @author Paul Schermerhorn
 *
 */
package com;

import java.io.Serializable;

/** Representation of constant logical form. */
public class Constant extends Variable implements Serializable {
    Object value = null;

    // can't change name of a Constant
    @Override
    public void setName(String n) {
    }

    // can't change type of a Constant
    @Override
    public void setType(String t) {
    }

    // the Symbol value of a constant is itself
    @Override
    public Symbol getValue() {
	return this;
    }

    public Object getConstantValue() {
	return value;
    }

    // can't change value of a Constant
    public void setValue(Object s) {
    }

    @Override
    public Object clone() {
        Constant cloned = new Constant(getName(), getType(), value);
        
        return cloned;
    }

    public Constant(Constant c) {
        super(c.getName(), c.getType());
    }

    public Constant(String n) {
        super(n, "entity");
    }

    public Constant(String n, String t) {
        super(n, t);
    }
    
    public Constant(String n, String t, Object v) {
        super(n, t);
        value = v;
    }
}
