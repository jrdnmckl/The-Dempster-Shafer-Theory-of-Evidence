/**
 * ADE 1.0 Copyright 1997-2010 HRILab (http://hrilab.org/)
 *
 * All rights reserved. Do not copy and use without permission. For questions
 * contact Matthias Scheutz at mscheutz@indiana.edu
 *
 * Symbol.java
 *
 * @author Paul Schermerhorn
 *
 */
package com;

import java.io.Serializable;

/**
 * Representation of symbol logical form.
 */
public class Symbol implements Serializable {

  private String name;

  /**
   * @author Rehj
   */
  public String getName() {
    return name;
  }

  /**
   * @author Rehj
   */
  // PWS: not sure this makes sense
  public void setName(String n) {
    name = n;
  }

  /**
   * Check whether the given object is equal to this one. Two Symbols are equal
   * if they're of the same class and their names are the same.
   *
   * @param o the Object to compare this with
   * @return true if they're equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    // needs to be the same class to be equal
    if (!getClass().equals(o.getClass())) {
      return false;
    }
    // otherwise, just matching on name
    return name.equalsIgnoreCase(((Symbol) o).getName());
  }

  /**
   * Calculate hash code for this Symbol. This is based only on the name's hash
   * code.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return 37 + name.hashCode();
  }

  @Override
  public String toString() {
    return name;
  }

  public String toCoreString() {
    return name;
  }
  
  public String toSExpression() {
    return name;
  }

  public Symbol(Symbol s) {
    name = s.name;
  }

  public Symbol(String n) {
    name = n;
  }

  public Symbol copy() {
    return new Symbol(name);
  }
}
