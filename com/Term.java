/**
 * ADE 1.0 
 * Copyright 1997-2010 HRILab (http://hrilab.org/) 
 * 
 * All rights reserved.  Do not copy and use without permission. 
 * For questions contact Matthias Scheutz at mscheutz@indiana.edu
 *
 * Term.java
 *
 * @author Paul Schermerhorn
 *
 */
package com;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

/** Representation of term logical form. */
public class Term extends Symbol implements Serializable {
	//EAK: should this be private ?
	protected ArrayList<Symbol> args;
	protected boolean isNegated;

	public boolean getIsNegated() {
		return isNegated;
	}

	public void negate() {
		isNegated = !isNegated;
	}

	public Term getNegatedTerm() {
		Term t = this.copy();
		t.negate();
		return t;
	}

	// PWS: not sure this makes sense
	public boolean add(Symbol a) {
		return args.add(a);
	}

	public Symbol get(int i) {
		return args.get(i);
	}

	//EAK: modified to return shallow copy instead of direct ref.
	public ArrayList<Symbol> getArgs() {
		return new ArrayList<Symbol>(args);
	}

	public ArrayList<Symbol> getArgsRef() {
                return args;
        }

	public ArrayList<Symbol> getArgsCopy() {
		ObjectOutputStream out = null;
		ArrayList<Symbol> newArgs = null;
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			out = new ObjectOutputStream(b);
			out.writeObject(args);
			out.flush();
			out.close();
			ObjectInputStream in = new ObjectInputStream(
							new ByteArrayInputStream(b.toByteArray()));
			newArgs = (ArrayList<Symbol>) in.readObject();
		} catch (ClassNotFoundException ex) {
			System.err.println("Error copying args: " + ex);
		} catch (IOException ex) {
			System.err.println("Error copying args: " + ex);
		}
		return newArgs;
	}

	/** @author Gordon */
	// Based on Derek's getVars method
	public Set<Variable> getVars() {
		Set<Variable> allVars = new HashSet<Variable>();
		for (Symbol s : args) {
			if (s instanceof Variable) {
				allVars.add((Variable) s);
			} else if (s instanceof Term) {
				allVars.addAll(((Term) s).getVars());
			}
		}
		return allVars;
	}

	/**@author Gordon */
	// Applies binding map for convenience. Different than Derek's function as we utilize the new Variable binding support  
	public void applyBindingMap(Map<Variable, Symbol> bindings) {
		Set<Variable> bVars = bindings.keySet();
		for (Symbol s : this.getArgs()) {
			if (s instanceof Variable) {
				if (bVars.contains(s)) {
					((Variable) s).setValue(bindings.get(s));
				}
			} else if (s instanceof Term) {
				((Term) s).applyBindingMap(bindings);
			}
		}
	}

	/**@author Tom */
	// Replaces variables with typed variables
	public void applyVarTypes(List<Variable> bindings) {
            //System.out.println("calling avt on "+this+" with bindings "+bindings);
            for (Symbol s : this.getArgs()) {
                //System.out.println("symbol "+s);
                if (s instanceof Variable) {
                    for(Variable v : bindings){                        
                        //System.out.println(v.getName()+" vs "+s.getName());
                        if(v.getName().equals(s.getName()))
                            ((Variable) s).setType(v.getType());
                    }
                } else if (s instanceof Term) {
                    ((Term) s).applyVarTypes(bindings);
                }
            }
            //System.out.println("Final result: "+this);
	}


	public Term copyWithNewBindings(Map<Variable, Symbol> bindings) {
		Term t = this.copy();
		t.applyBindingMap(bindings);
		return t;
	}

	public Map<Variable, Symbol> generateBinding(Term instantiatedTerm) {

		ArrayList<Symbol> args = this.getArgs();
		ArrayList<Symbol> iArgs = instantiatedTerm.getArgs();

		if (!this.getName().equals(instantiatedTerm.getName())) {
			return null;
		} else if (args.size() != iArgs.size()) {
			return null;
		}

		Map<Variable, Symbol> retMap = new HashMap<Variable, Symbol>();
		for (int i = 0; i < args.size(); i++) {
			Symbol s = args.get(i);
			if (s instanceof Variable) {
				retMap.put((Variable) s, iArgs.get(i));
			}
		}

		return retMap;
	}

	/** @author Rehj */
	// PWS: not sure this makes sense
	public Symbol remove(int i) {
		return args.remove(i);
	}

    // RC: I know, I know, this doesn't make sense either...
    public void set(int i,Symbol newArg) {
	args.set(i,newArg);
	}

	public int size() {
		return args.size();
	}

	/**
	 * Check whether the given object is equal to this one.  Two Symbols are equal 
	 * if they're of the same class, their names are the same, and their args are
	 * the same.
	 * @param o the Object to compare this with
	 * @return true if they're equal, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		// needs to be the same class to be equal
		if (!getClass().equals(o.getClass())) {
			return false;
		}
		// matching on name
		if (!getName().equalsIgnoreCase(((Term) o).getName())) {
			return false;
		}
		//match args (order of args matters)
		final List<Symbol> other_args = ((Term) o).getArgs();
		int size = args.size();
		if (args.size() != other_args.size()) {
			return false;
		}
		for (int i = 0; i < size; ++i) {
			if (!args.get(i).equals(other_args.get(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Calculate hash code for this Term.
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + super.hashCode();  //"name" hashCode
		result = result * prime + args.hashCode(); //arg hashCodes

		return result;
	}

    /**
     * Constructs a String representation of the Term.
     * If the term is negated, then it is wrapped in a not().
     * @return String of the form name(args...) or not(name(args...))
     */
	@Override
	public String toString() {
      StringBuilder sb = new StringBuilder();
      if (isNegated) {
			sb.append("not(");
      }
      sb.append(getName()).append('(');
      String sp = "";
      for (Symbol a : args) {
        sb.append(sp).append(a.toString());
        sp = ",";
      }
      sb.append(")");
      if (isNegated) {
        sb.append(')');
      }
      return sb.toString();
	}

    /**
     * Constructs a String representation of the Term.
     * @return String of the form name(args...)
     */
	@Override
	public String toCoreString() {		
      StringBuilder sb = new StringBuilder(getName());
      sb.append('(');
      String sp = "";
      for (Symbol a : args) {
        sb.append(sp).append(a.toCoreString());
        sp = ",";
      }
      sb.append(")");
      return sb.toString();
	}

    /**
     * Converts the term to an S-Expression.
     * This is used for when communicating with LISP.
     * @return String is of the form (name args...)
     */
    @Override
    public String toSExpression() {
      StringBuilder sb = new StringBuilder("(");
      sb.append(getName());
      for (Symbol a : args) {
        sb.append(' ').append(a.toSExpression());
      }
      sb.append(')');
      return sb.toString();
    }

	public Term(String n) {
		super(n);
		args = new ArrayList<Symbol>();
	}

	public Term(Term t) {
		super(t.getName());
		args = new ArrayList<Symbol>();
		for (Object a : t.args) {
			// use reflection to preserve the real classes of arguments
			Class<?> cls = a.getClass();
			try {
				Constructor<?> cnst = cls.getDeclaredConstructor(cls);
				args.add((Symbol) cnst.newInstance(a));
			} catch (Exception e) {
				System.err.println("Term: error creating copy of argument " + a + ": " + e);
				// fall back on generic Symbol
				args.add(new Symbol((Symbol) a));
			}
		}
	}

	public Term(String n, List<Symbol> a) {
		super(n);
		addConstructorArgs(a);
	}

        public Term(String n, Symbol... a) {
		super(n);
                List<Symbol> aList = new ArrayList();
                for (Symbol currArg : a) {
                  aList.add(currArg);
                }
		addConstructorArgs(aList);
	}

	public Term(String n, String... a) {
		super(n);
		args = new ArrayList<Symbol>();
		for (String s : a) {
                        int ind = s.indexOf(":");
                        if (ind < 0) {
                                //System.err.println("Arguments to Terms should be typed: "+s+" in "+n);
                                args.add(new Symbol(s));
                        } else {
                                String aname = s.substring(0, ind);
                                String atype = s.substring(ind+1);
                                if (aname.startsWith("?")) {
                                        args.add(new Variable(aname, atype));
                                } else {
                                        args.add(new Constant(aname, atype));
                                }
                        }
		}
	}

        @Override
	public Term copy() {
		return new Term(this);
	}

        private void addConstructorArgs(List<Symbol> a) {
          args = new ArrayList<Symbol>();
          for (Object arg : a) {
            // use reflection to preserve the real classes of arguments
            Class<?> cls = arg.getClass();
            try {
              Constructor<?> cnst = cls.getDeclaredConstructor(cls);
              args.add((Symbol) cnst.newInstance(arg));
            } catch (Exception e) {
              System.err.println("Term: error creating copy of argument " + arg + ": " + e);
              e.printStackTrace(); //TEW: Yes Please.
              // fall back on generic Symbol
              args.add(new Symbol((Symbol) arg));
            }
          }
        }
}
