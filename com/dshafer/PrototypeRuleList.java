/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dshafer;

import java.util.ArrayList;
import com.*;
import com.slug.dialogue.prag.PragUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import utilities.Util;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
/**
 *
 * @author gbriggs
 */
public class PrototypeRuleList extends ArrayList<PrototypeRule> {
    
    public PrototypeRuleList() {
        super();
    }
   
    public List<Predicate> getLhsPredicates() {
	List<Predicate> retList = new ArrayList<Predicate>();
	for(PrototypeRule rule: this) {
	    List<Predicate> lhs = rule.getLhs();
	    for(Predicate p: lhs) {
		boolean alreadyExists = false;
		for(Predicate p2: retList) {
		    if(p.toCoreString().equals(p2.toCoreString())) {
			alreadyExists = true;
		        break;
		    }
		}
                if(!alreadyExists) retList.add(p);
	    }
	}
	
	return retList;
    }
 
    public List<DSFrame> getFrames(List<Predicate> context) {
        List<DSFrame> retList = new ArrayList<DSFrame>();
        /*for(PrototypeRule rule: this) {
            if(rule.isApplicable(context)) {
                retList.add(rule.getRhs());
            }
        }*/
	for(PrototypeRule rule: this) {
	    retList.addAll(rule.getApplicableFrames(context));
        } 
        return retList;
    }
    
    public Map<List<String>,List<DSFrame>> getFrameLists(List<Predicate> context) {
        Map<List<String>,List<DSFrame>> retMap = new HashMap<List<String>,List<DSFrame>>();
        List<DSFrame> list = this.getFrames(context);
        for(DSFrame dsf: list) {
            List<String> hypotheses = dsf.getHypotheses();
            Collections.sort(hypotheses);
            
            
        }
        return retMap; 
    }
    
    public void parseFile(String filename) {
        File f = new File(filename); 
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = "#";
            while(line != null) {
               if(!line.startsWith("#")) {
                  PrototypeRule pr = new PrototypeRule();
                  pr.parseRule(line);
                  this.add(pr);
               }
               line = br.readLine();
           }
            
       } catch (FileNotFoundException fnf) {
           System.err.println("PrototypeRuleList.parseFile - file not found: " + filename);
           fnf.printStackTrace();
       } catch (IOException ioe) {
           System.err.println("PrototypeRuleLise.parseFile - IOException");
           ioe.printStackTrace();
       }
        
        
       
        
   }
    
   /********************************/
   public static void main(String[] args) {
       PrototypeRuleList ruleList = new PrototypeRuleList();
       ruleList.parseFile("list.rules");
       for(PrototypeRule pr: ruleList) {
           System.out.println(pr.toString());
        //   System.out.println(pr.getRhs().toString());
       }
       
       //////
       List<Predicate> testList = new ArrayList<Predicate>();
       //testList.add(Util.functionPredVars("not_allowed(color(east,red))"));
       //testList.add(Util.functionPredVars("want(human,bel(self,color(east,red)),uncertain)"));
       //testList.add(Util.functionPredVars("trust(self,human)"));
       //testList.add(Util.functionPredVars("is_uncertain(human,color(east,red))"));
       testList.add(Util.functionPredVars("want(human,bel(self,at(evan,office)))"));       

       List<DSFrame> frames = ruleList.getFrames(testList);
       System.out.println(frames);
       
       //System.out.println(frames.get(0).dempsterCombination(frames.get(1)));
       
   }
    
    
}
