// Gordon's Barebones DS Code
// Dec 2012
package com.dshafer; 

import com.dshafer.*;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.BitSet;
import utilities.Util;
import com.*;

public class DSFrame {

    private String frameName;
    private ArrayList<String> hypotheses;
    private Map<BitSet,Double> masses;
	
    public DSFrame() {
        masses = new HashMap<BitSet,Double>();
        hypotheses = new ArrayList<String>();
    }
	
    public void constructFrame(String ... hypos) {
        HashSet<String> hypoSet = new HashSet<String>();
        for(String h: hypos) {
            hypoSet.add(h);
        }
        constructFrame(hypoSet);
    }
	
    public void constructFrame(Set<String> hypos) {
        hypotheses.clear();
        for(String s: hypos) {
            hypotheses.add(s);
        }
		
        List<BitSet> keys = DSFrame.getPowerSetKeys(hypotheses.size());
		
        for(BitSet bs: keys) {
            if(bs.cardinality() < hypotheses.size()) {
                masses.put(bs, new Double(0.0));
            } else {
                masses.put(bs, new Double(1.0));
            }
        }
    }

    public void constructFrame(List<String> hypos) {
        hypotheses.clear();
        for(String s: hypos) {
            hypotheses.add(s);
        }
		
        List<BitSet> keys = DSFrame.getPowerSetKeys(hypotheses.size());
        for(BitSet bs: keys) {
            if(bs.cardinality() < hypotheses.size()) {
                masses.put(bs, new Double(0.0));
            } else {
                masses.put(bs, new Double(1.0));
            }
        }
    }
        
    public String getFrameName() {
        return frameName;
    }

    public void setFrameName(String frameName) {
        this.frameName = frameName;
    }
	   
        
    public static List<BitSet> getPowerSetKeys(int num) {
        List<BitSet> retList = new ArrayList<BitSet>();
        List<BitSet> tmpList = new ArrayList<BitSet>();
		
        BitSet initBSet = new BitSet();
        retList.add(initBSet);
		
        for(int i=0; i<num; i++) {
            for(BitSet bs: retList) {
                BitSet newBs1 = (BitSet)bs.clone();
                BitSet newBs2 = (BitSet)bs.clone();
                newBs1.set(i,false);
                newBs2.set(i,true);
                tmpList.add(newBs1);
                tmpList.add(newBs2);
            }
            retList.clear();
            retList.addAll(tmpList);
            tmpList.clear();
        }
		
        CardinalityComparator cc = new CardinalityComparator();
        Collections.sort(retList,cc);
		
        return retList;
    }
	
    private int getIndex(String hypo) {
        for(int i=0; i<hypotheses.size(); i++) {
            if(hypotheses.get(i).equals(hypo)) {
                return i;
            }
        }
        return -1;
    }
	
    public BitSet getBitSetForm(String ... hs) {
        List<String> hList = new ArrayList<String>();
        for(String s: hs) {
            hList.add(s);
        }
        return getBitSetForm(hList);
    }
	
    public BitSet getBitSetForm(List<String> hs) {
        BitSet retSet  = new BitSet();
        for(String s : hs) {
            int index = getIndex(s);
            if(index >= 0) {
                retSet.set(index, true);
            }
        }
        return retSet; 
    }
	
    public void normalizeMasses() {
        double sum = 0.0;
        for(Double d: masses.values()) {
            sum+= d.doubleValue();
        }
		
        if(sum > 0.0) {
            for(BitSet key: masses.keySet()) {
                masses.put(key, masses.get(key)/sum);
            }
        } else {
            BitSet bs = new BitSet();
            bs.set(0,hypotheses.size(),true);
            masses.put(bs, new Double(1.0));
        }
    }
	
    public void normalizeMassesAlt() {
            
    }
        
    public void clearMasses() {
        for(BitSet key: masses.keySet()) {
            masses.put(key,0.0);
        }
    }
	
    public void setMasses(Map<BitSet,Double> masses) {
        this.masses = masses;
    }

    public Map<BitSet,Double> getMasses() {
        return masses;
    } 

    public ArrayList<String> getHypotheses() {
        return hypotheses;
    }
        
    public boolean typeEquals(DSFrame frame) {
        if(frame.hypotheses.size() != this.hypotheses.size()) return false;
        Collections.sort(frame.hypotheses);
        Collections.sort(hypotheses);
        for(int i=0; i<hypotheses.size(); i++) {
            if(!hypotheses.get(i).equals(frame.hypotheses.get(i))) {
                return false;
            }
        }
        return true;
    }
	
    public void setMass(BitSet bs, double d) {
        masses.put(bs, new Double(d));
    }
	
    public void setMass(List<String> hs, double d) {
        BitSet key = getBitSetForm(hs);
        setMass(key,d);
    }
	
    public void setMass(double d, String...hs) {
        List<String> hList = new ArrayList<String>();
        for(String s: hs) {
            hList.add(s);
        }
        setMass(hList, d);
    }
	
    public double getMass(BitSet key) {
        return masses.get(key);
    }

    public double getBel(String h){return getBel(getBitSetForm(h));}
    public double getPl(String h){return getPl(getBitSetForm(h));}
    public double getUncertainty(String h){return getUncertainty(getBitSetForm(h));}

    public double getBel(BitSet bs) {
        double sum = 0.0;
        for(BitSet bs2: masses.keySet()) {
            BitSet tmp = (BitSet)bs2.clone();
            tmp.and(bs);
            //System.err.println("bs = " + bs);
            //System.err.println("bs2 = " + bs2);
            //System.err.println("tmp = " + tmp);
            if(tmp.equals(bs2)) {
                sum += masses.get(bs2);
            }
        }
        return sum;
    }
	
    public double getPl(BitSet bs) {
        double sum = 0.0;
        for(BitSet bs2: masses.keySet()) {
            //System.err.println("bs = " + bs);
            //System.err.println("bs2 = " + bs2);
            //System.err.println("intersect? = " + bs2.intersects(bs));
            if(bs2.intersects(bs)) {
                //	System.err.println("bs = " + bs);
		//		System.err.println("bs2 = " + bs2);
                sum += masses.get(bs2);
            }
        }
        return sum;
    }
	
    public double getUncertainty(BitSet bs) {
        return getPl(bs)-getBel(bs);
    }

    public List<BitSet> focalElements() {
        List<BitSet> retList = new ArrayList<BitSet>();
        for(BitSet element: masses.keySet()) {
            double mass = getMass(element);
            if(mass > 0.0) {
                retList.add(element);
            } 
        }
        return retList;
    }

    public double faginHalpernConditionalMass(BitSet a, BitSet b) {
        /*System.out.println("a = " + a);
        System.out.println("a.cardinality() = " + a.cardinality() );
        System.out.println("b = " + b);
        if(a.cardinality() == 1) return faginHalpernConditional(a,b);
        else if(a.cardinality() > 1) {
            double retSum = faginHalpernConditional(a,b);
            List<BitSet> allTheSets = getPowerSetKeys(this.getHypotheses().size());
            for(BitSet bs: allTheSets) {
                if(bs.cardinality() == a.cardinality()-1){
                    BitSet tempBits = (BitSet)a.clone();
                    tempBits.and(bs);
                    if(tempBits.equals(bs)) {
                        System.out.println("bs = " + bs);
                        System.out.println("tempBits = " + tempBits);
                        retSum+= faginHalpernConditionalMass(bs,b);                    
                        System.out.println("add = " + faginHalpernConditionalMass(bs,b));
                        System.out.println("retSum = " + retSum);
                    }
                }
            }
            return retSum;
        }*/
        
        List<BitSet> keys = getPowerSetKeys(this.getHypotheses().size());
        double retSum = 0.0;
        
        for(BitSet bs: keys) {
            BitSet tempBits = (BitSet)b.clone();
                tempBits.and(bs);
                if(tempBits.equals(bs)) {
                    double cardDiff = Math.abs(b.cardinality()-tempBits.cardinality());
                    double partMass = Math.pow(-1,cardDiff);
                    retSum =  partMass * faginHalpernConditional(a,tempBits);
                }
        }
        
        
        return retSum;
    }

    public double faginHalpernConditional(BitSet a, BitSet b) {
        if(b.cardinality() <= 0) return 0.0;
                
		
        BitSet union1 = (BitSet)a.clone();
        BitSet union2 = (BitSet)a.clone(); 
        BitSet notB = (BitSet)b.clone();

        // System.out.println("union1 = " + union1);
        // System.out.println("union2 = " + union2);

        union1.and(b);
        notB.flip(0,hypotheses.size());
        union2.and(notB);

		
        // System.out.println("a = " + a);
        // System.out.println("b = " + b);
        // System.out.println("notB = " + notB);
        // System.out.println("union1 = " + union1);
        // System.out.println("union2 = " + union2);
		

        double numerator = getBel(union1);
        double denominator = numerator + getPl(union2);
		
        /*
          System.out.println("faginHalpernCondition.numerator = " + numerator);
          System.out.println("faginHalpernCondition.denominator = " + denominator);
        */

			
        if(numerator > 0.0) {
            return numerator/denominator;
        } else return 0.0;
    }

    private Map<BitSet,Double> convertBelToMass(Map<BitSet,Double> bels) {
        Map<BitSet,Double> retMap = new HashMap<BitSet,Double>();

        for(BitSet a: bels.keySet()) {
            double mass = 0.0;
            for(BitSet b: bels.keySet()) {
                BitSet bTmp = (BitSet)b.clone();
                bTmp.and(a);
                if(bTmp.equals(b)) {
                    double cardDiff = Math.abs(a.cardinality()-b.cardinality());
                    double partMass = Math.pow(-1,cardDiff)*bels.get(b);
                    mass+=partMass;
                }
            }
            retMap.put(a,mass);
        }
        return retMap; 
    }

    private double conditionalUpdateBel(DSFrame f2, double alpha, BitSet bKey) {
        double alphaSum = alpha*getBel(bKey);
        double betaSum = 0.0;
        for(BitSet aKey: f2.focalElements()) {
            betaSum += (1.0-alpha)/f2.focalElements().size() * f2.faginHalpernConditionalMass(aKey, bKey);
        }
        
          System.out.println("conditionalUpdateBel.bKey = " + bKey);
          System.out.println("conditionalUpdateBel.alphaSum = " + alphaSum);
          System.out.println("conditionalUpdateBel.betaSum = " + betaSum); 

        return alphaSum + betaSum;	
    }

    public DSFrame conditionalUpdate(DSFrame f2, double alpha) {
        //System.out.println("Adapting "+this+" based on "+f2+" by "+alpha);
            
        DSFrame retFrame = new DSFrame();
        retFrame.constructFrame(this.getHypotheses());	
        List<BitSet> keys = getPowerSetKeys(this.getHypotheses().size());

        Map<BitSet,Double> bels = new HashMap<BitSet,Double>();

        for(BitSet key: keys) {
            double bel = conditionalUpdateBel(f2, alpha, key);
            bels.put(key,bel);	
        }
		
        retFrame.setMasses(convertBelToMass(bels));
        //retFrame.normalizeMasses();
        //System.out.println("yields "+retFrame);
        return retFrame;
    }

    private static double conditionalFusionBel(DSFrame f1, DSFrame f2, BitSet bKey, double k1, double k2) {
        double k1sum = 0.0;
        double k2sum = 0.0;
        for(BitSet aKey: f1.focalElements()) {
            k1sum += 1.0/f1.focalElements().size() * f1.faginHalpernConditional(aKey,bKey);
        }
        for(BitSet aKey: f2.focalElements()) {
            k2sum += 1.0/f2.focalElements().size() * f2.faginHalpernConditional(aKey,bKey);
        }
        System.out.println(k1+"*"+k1sum+"+"+k2+"*"+k2sum);
        return k1*k1sum+k2*k2sum;
    }

    public static DSFrame conditionalFusion(DSFrame f1, DSFrame f2) {
        return conditionalFusion(f1,f2,0.5,0.5);
    }

    public static DSFrame conditionalFusion(DSFrame f1, DSFrame f2, double k1, double k2) {
        DSFrame retFrame = new DSFrame();
        retFrame.constructFrame(f1.getHypotheses());
		
        List<BitSet> keys = getPowerSetKeys(retFrame.getHypotheses().size());
        Map<BitSet,Double> bels = new HashMap<BitSet,Double>();

        System.out.println("Fusing 50/50: "+f1+", "+f2);
        for(BitSet key: keys) {
            System.out.println("Fusing for key: "+key);
            double bel = conditionalFusionBel(f1,f2,key,k1,k2);
            bels.put(key,bel);
        }
        retFrame.setMasses(f1.convertBelToMass(bels));

        return retFrame;
    }
	
    public DSFrame dempsterCombination(DSFrame f2) {
        //TODO: check to make sure the frame is for the same set of hypotheses
        DSFrame newFrame = new DSFrame();
        newFrame.constructFrame(new HashSet<String>(this.hypotheses));
		
        for(BitSet a: this.masses.keySet()) {
            double sum = 0.0;
            double kscore = 0.0;
            for(BitSet b: this.masses.keySet()) {
                for(BitSet c: this.masses.keySet()) {
                    double prod = this.masses.get(b) * f2.getMass(c);
                    if(b.intersects(c)) {
                        kscore += prod;
                    }
                    BitSet tmpB = (BitSet)b.clone();
                    //BitSet tmpC = (BitSet)c.clone();
                    tmpB.and(c);
                    if(tmpB.equals(a) && a.cardinality() > 0) {
                        sum += prod;
                    }
                }
            }
            double mass = 1/(1-kscore) * sum;
            newFrame.setMass(a, mass);
        }
		
        newFrame.normalizeMasses();
		
        return newFrame;
    }
	
    public static DSFrame weightedAvgCombination(Map<DSFrame,Double> frames) {
        if(frames.isEmpty()) return null;
        DSFrame keyFrame = (DSFrame)frames.keySet().toArray()[0];
        DSFrame newFrame = new DSFrame();
		
        // TODO: check that all frames are the same 
        for(BitSet key: keyFrame.masses.keySet()) {
            double sum = 0.0;
            for(DSFrame frame: frames.keySet()) {
                sum += frame.getMass(key)*frames.get(frame);
            }
            sum /= frames.size();
            newFrame.setMass(key, sum);
        }
		
        newFrame.normalizeMasses();
        return newFrame;
    }
        
    private static List<BitSet> getSupersets(DSFrame frame, BitSet key) {
        List<BitSet> retList = new ArrayList<BitSet>();
        for(BitSet bs: frame.masses.keySet()) {
            BitSet tmp = (BitSet)bs.clone();
            tmp.and(key);
            if(tmp.equals(key)) {
                retList.add(bs);
            }
        }
        return retList; 
    } 
        
    private static List<List<BitSet>> getIntersectingFrames(DSFrame keyFrame, BitSet A, int num) {
        List<List<BitSet>> retList = new ArrayList<List<BitSet>>();
        List<BitSet> supersetKeys = getSupersets(keyFrame,A); 
            
        // initialize list
        for(BitSet bs: supersetKeys) {
            List<BitSet> newList = new ArrayList<BitSet>();
            newList.add(bs);
            retList.add(newList);
        }
            
        do {
            List<BitSet> list = retList.remove(0);
            for(BitSet bs: supersetKeys) {
                List<BitSet> newList = new ArrayList<BitSet>(list);
                newList.add(bs);
                retList.add(newList);
            }
        } while(retList.get(0).size() < num);
         
	List<List<BitSet>> finalList = new ArrayList<List<BitSet>>();  
	for(List<BitSet> l: retList) {
	   BitSet baseSet = new BitSet(keyFrame.getHypotheses().size());
           baseSet.set(0,keyFrame.getHypotheses().size());
           for(BitSet bs: l) {
	       baseSet.and(bs);
           }
           if(baseSet.equals(A)) {
               finalList.add(l);
           }
        }
 
        return finalList;
    }
    public static DSFrame yagerCombination(Map<DSFrame,Double> frames) {
        if(frames.isEmpty()) return null;
        DSFrame keyFrame = (DSFrame) frames.keySet().toArray()[0];
        DSFrame newFrame = new DSFrame();
        newFrame.constructFrame(keyFrame.getHypotheses());
        double qNull = 0.0; 
        Map<BitSet,Double> qSums = new HashMap<BitSet,Double>();                  
            
        // TODO: check that all frames are the same
        for(BitSet key: keyFrame.masses.keySet()) {
            double sum = 0.0; 
                
            List<List<BitSet>> intersectingFrames = getIntersectingFrames(keyFrame, key, frames.size());
            System.out.println("intersectingFrames " + key + " : " + intersectingFrames);
	    for(List<BitSet> list: intersectingFrames) {
                double product = 1.0;
                for(int i=0; i<list.size(); i++) {
                    DSFrame tmpFrame = (DSFrame)frames.keySet().toArray()[i];
                    product *= tmpFrame.getMass(list.get(i));
                }
                sum += product; 
            }
                
            if(key.cardinality() == 0) {
                qNull = sum; 
                System.out.println("qNull = " + qNull);
            } else {
                qSums.put(key, sum);
            }
        }
            
        newFrame.setMass(new BitSet(),0.0);
        for(BitSet key: qSums.keySet()) {
            newFrame.setMass(key, qSums.get(key));
	    if(key.cardinality() == newFrame.getHypotheses().size()) {
                newFrame.setMass(key,qSums.get(key)+qNull);
            }
        }
            
        newFrame.normalizeMasses();
        return newFrame; 
    }
	
    public static DSFrame parseString(String def) {
        String[] tokens = def.split(";");
        if(tokens.length > 0) {
            String hypoStr = tokens[0].trim();
            Predicate hypos = Util.functionPredVars(hypoStr);
            if(hypos.getName().equalsIgnoreCase("hypotheses")) {
                DSFrame newFrame = new DSFrame();
                Set<String> hypoStrs = new HashSet<String>();
                for(Symbol s: hypos.getArgs()) {
                    if(s instanceof Term) {
                        Term t = (Term)s;
                        hypoStrs.add(t.toCoreString());
                    } else {
                        hypoStrs.add(s.toCoreString());
                    }
                }
                newFrame.constructFrame(hypoStrs);
                newFrame.clearMasses();
                for(int i=1; i < tokens.length ; i++) {
                    String tmp = tokens[i].trim();
                    String[] tmpSplit = tmp.split("=");
                    String keyStr = tmpSplit[0];
                    //System.err.println("keyStr = " + keyStr);
                    double val = Double.parseDouble(tmpSplit[1]);
                    Predicate tmpPred = Util.functionPredVars(keyStr);
                    List<String> key = new ArrayList<String>();
                    for(Symbol s: tmpPred.getArgs()) {
                        if(s instanceof Term) {
                            Term t = (Term)s;
                            key.add(t.toCoreString());
                        } else {
                            key.add(s.toCoreString());
                        }
                    }
                    newFrame.setMass(key, val);
                }
                newFrame.normalizeMasses();
                return newFrame; 
            }
        }
        return null;
    }
            
        
    public String toString() {
        String retStr = "FrameOfDiscernment(begin)\n";
        ArrayList<BitSet> sortedkeyset = new ArrayList<BitSet>(masses.keySet());
        CardinalityComparator cc = new CardinalityComparator();
        Collections.sort(sortedkeyset,cc);
        retStr+=("Key: ");
        for(int i = 0; i < hypotheses.size(); i++)
            retStr+=(i+": "+hypotheses.get(i)+"\n");
                    
        for(BitSet bs: sortedkeyset) {
            retStr += bs.toString() + " \t\t : " + masses.get(bs) + "\t : " + getBel(bs) + "\t :" + getPl(bs) + "\n";
        }
        retStr += "FrameOfDiscernment(end)\n";
        return retStr;
    }
	
    // main method - for testing
    public static void main(String[] args) {
        DSFrame frame = new DSFrame(); 
        DSFrame f2 = new DSFrame();
        //DSFrame f3 = new DSFrame();
                
        frame.constructFrame("sub","peer");
	
        frame.clearMasses();
        //frame.setMass(.35,"sub");
        //frame.setMass(.2,"peer");
        //frame.setMass(.1,"sup");
        frame.setMass(.27, "sub");
        frame.setMass(.73, "peer");
		
        frame.normalizeMasses();
		
        f2.constructFrame("sub","peer");
		
        f2.clearMasses();
        f2.setMass(.98,"sub");
        //f2.setMass(.5,"peer","sub");
		
        //f2.setMass(.1,"sup");
        f2.setMass(.02, "peer");
        f2.normalizeMasses();
		
                
        //f3.constructFrame("sub","peer","sup");
        //f3.clearMasses();
        //f3.setMass(.9,"sub");
        //f3.setMass(.3,"peer","sup");
        //f3.setMass(.1,"peer","sub","sup");
        //f3.normalizeMasses();
                
        System.out.println(frame);
        System.out.println(f2);
        //System.out.println(frame.dempsterCombination(f2));
        //System.out.println(frame.conditionalUpdate(f2,0.5));
        System.out.println(DSFrame.conditionalFusion(frame,f2));
                
        Map<DSFrame,Double> frameMap = new HashMap<DSFrame,Double>();
        frameMap.put(frame, 1.0);
        frameMap.put(f2, 1.0);
        //frameMap.put(f3, 1.0);
		
        System.out.println("test");
               
        System.out.println("yagerComb : " + DSFrame.yagerCombination(frameMap));
        //System.out.println(DSFrame.weightedAvgCombination(frameMap));
    }
}
