/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dshafer;

import java.util.List;
import com.*;
import com.slug.dialogue.prag.PragUtil;
import utilities.Util;
import java.util.ArrayList; 
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.BitSet;

/**
 *
 * @author gbriggs
 */
public class PrototypeRule {
    
    private List<Predicate> lhs;
    private DSFrame rhs; 
    
    public PrototypeRule() {
        lhs = new ArrayList<Predicate>();
    }

    public List<Predicate> getLhs() {
	return this.lhs;
    }
    
    public DSFrame getRhs() {
        return this.rhs;
    }
 
    public boolean isApplicable(List<Predicate> facts) {
        for(Predicate p1: lhs) {
            boolean found = false; 
            Map<Variable,Symbol> tmpBinding;
            for(Predicate p2: facts) {
                tmpBinding = PragUtil.getTermBindings(p1, p2);
                if(tmpBinding != null) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                return false;
            }
        }
        return true; 
    }

    public List<DSFrame> getApplicableFrames(List<Predicate> facts) {
       System.out.println("facts = " + facts);

       if(lhs.isEmpty()) return null;

       List<DSFrame> retList = new ArrayList<DSFrame>();	       
       List<Map<Variable, Symbol>> allBindings = new ArrayList<Map<Variable,Symbol>>();     

       for(Predicate p: facts) {
          Map<Variable,Symbol> tmpBinding = PragUtil.getTermBindings(lhs.get(0),p);
          if(tmpBinding != null) {
	     allBindings.add(tmpBinding);
	  }
       }
	
       for(int i=1; i<lhs.size(); i++) {
	  List<Map<Variable,Symbol>> newList = new ArrayList<Map<Variable,Symbol>>();
	  for(Map<Variable,Symbol> binding: allBindings) {
	     for(Predicate p: facts) {
	        Map<Variable,Symbol> tmpBinding = PragUtil.getBindingsTermHelper(binding,lhs.get(i), p);
                if(tmpBinding != null) {
                   newList.add(tmpBinding);
                }
	     }
	  }
          allBindings = newList;
       }

       for(Map<Variable,Symbol> binding: allBindings) {
           List<String> hypotheses = rhs.getHypotheses(); 
           List<String> boundHypotheses = new ArrayList<String>();
           Map<BitSet,Double> masses = rhs.getMasses();
	   DSFrame frame = new DSFrame();
	   for(String s: hypotheses) {
	      if(s.indexOf('(')!=-1) { 
		  Predicate p = Util.functionPredVars(s);
                  Predicate boundPred = new Predicate(PragUtil.getBoundTerm(binding,p));
                  boundHypotheses.add(boundPred.toCoreString()); 
              } else {
		  boundHypotheses.add(s);
	      }
           }
           frame.constructFrame(boundHypotheses);

	   /*
	   for(int i=0; i<hypotheses.size(); i++) {
             String hypo1 = hypotheses.get(i);
           }*/

	   for(BitSet bs: masses.keySet()) {
              frame.setMass(bs, masses.get(bs));
 	   }	   

	   retList.add(frame);
       }

       //System.out.println("PrototypeRule.retList = " + retList);

       return retList;
    }
    
    public void parseRule(String str) {
        String[] split = str.split(":=");
        if(split.length > 1) {
            String lhsStr = split[0].trim();
            String rhsStr = split[1].trim();
            
            /* parse lhs Predicates */
            String[] lTokens = lhsStr.split("&");
            for(String s: lTokens) {
                Predicate p = Util.functionPredVars(s.trim());
                lhs.add(p);
            }
            
            /* parse rhs DSFrame */
            this.rhs = DSFrame.parseString(rhsStr);
            
        }
        
    }
    
    public String toString() {
        String retStr = "rule: " + lhs.toString();
        return retStr;
    }
    
    /******************/
    public static void main(String[] args) {
        
    }
    
}
