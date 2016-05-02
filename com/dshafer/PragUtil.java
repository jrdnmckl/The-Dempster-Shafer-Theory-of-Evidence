package com.dshafer;

import com.*;
import com.Symbol;
import com.Term;
import com.Variable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import utilities.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PragUtil {
    private static Log log = LogFactory.getLog(PragUtil.class);
    ///////////////////////////////////////////////////////////
    // Get bound version methods
    ///////////////////////////////////////////////////////////
    public static Symbol getBoundSymbol(Map<Variable, Symbol> bindings, Symbol sym) {
        if(sym instanceof Variable) {
            Symbol tmp = bindings.get((Variable)sym);
            if(tmp != null) return tmp;
            else return sym;
        } else if(sym instanceof Term) {
            return getBoundTerm(bindings,(Term)sym);
        }
        return sym;
    }

    public static Symbol getBoundSymbolVars(Map<Variable, Symbol> bindings, Symbol sym) {
        if(sym instanceof Variable) {
            Variable sv = ((Variable)sym);
            for(Variable v : bindings.keySet()){
                if(v.getName().equals(sv.getName())&&
                   (v.getType().equals(sv.getType()) ||
                    v.getType().equals("") ||
                    sv.getType().equals("")))
                    return bindings.get(v);
            }            
            return sym;
        } else if(sym instanceof Term) {
            return getBoundTermVars(bindings,(Term)sym);
        }
        return sym;
    }


    public static Term getBoundTerm(Map<Variable,Symbol> bindings, Term term) {
        String name = term.getName();
        ArrayList<Symbol> args = term.getArgsCopy();
     
        for(int i=0; i<args.size(); i++) {
            Symbol s = args.get(i);
            args.set(i,getBoundSymbol(bindings,s)); 
        }
        //log.debug("GBT: b:"+bindings+"\tt:"+term+"\tT:"+new Term(name, args));
        return new Term(name, args);
    }


    public static Symbol reverseSymbolVars(Symbol sym) {
        if(sym instanceof Variable) {
            Variable sv = ((Variable)sym);
            return new Variable(sv.getType(),sv.getName());
        } else if(sym instanceof Term)
            return reverseTermVars((Term) sym);
        return sym;
    }

    public static Term reverseTermVars(Term term) {
        String name = term.getName();
        ArrayList<Symbol> args = term.getArgsCopy();

        for(int i=0; i<args.size(); i++) {
            Symbol s = args.get(i);
            args.set(i,reverseSymbolVars(s));
        }
        //System.out.println("GBT: b:"+bindings+"\tt:"+term+"\tT:"+new Term(name, args));
        return new Term(name, args);
    }

    public static Term getBoundTermVars(Map<Variable,Symbol> bindings, Term term) {
        String name = term.getName();
        ArrayList<Symbol> args = term.getArgsCopy();
    
        for(int i=0; i<args.size(); i++) {
            Symbol s = args.get(i);
            args.set(i,getBoundSymbolVars(bindings,s)); 
        }
        //System.out.println("GBT: b:"+bindings+"\tt:"+term+"\tT:"+new Term(name, args));
        return new Term(name, args);
    }

    public static Term fillPredicate(Map<Variable,Symbol> bindings, Term term) {
        String name = term.getName();
        ArrayList<Symbol> args = term.getArgsCopy();
    
        for(int i=0; i<args.size(); i++) {
            Symbol s = args.get(i);
            args.set(i,fillSymbol(bindings,s)); 
        }
        //System.out.println("GBT: b:"+bindings+"\tt:"+term+"\tT:"+new Term(name, args));
        return new Term(name, args);
    }

    public static Symbol fillSymbol(Map<Variable, Symbol> bindings, Symbol s) {
        if(s instanceof Variable) {
            for(Variable v: bindings.keySet()){
                if(v.getName().equals(((Variable)s).getType()))
                    return bindings.get(v);
            }
            return s;
        } else if(s instanceof Term) {
            return fillPredicate(bindings,(Term)s);
        }
        return s;
    }


    public static Utterance getBoundUtterance(Map<Variable,Symbol> bindings, Utterance u) {
        Symbol newSemantics = getBoundSymbol(bindings, u.semantics);

        Utterance retUtt = new Utterance(newSemantics);
        retUtt.type = u.type;
        retUtt.speaker = getBoundSymbol(bindings, u.speaker);
   
        ArrayList<Symbol> newListeners = new ArrayList<Symbol>();
        for(Symbol list: u.listener) {
            newListeners.add(getBoundSymbol(bindings, list));
        }
        retUtt.listener = newListeners; 
   
        if(u.adverbs != null) {
            ArrayList<Symbol> newMods = new ArrayList<Symbol>();
            for(Symbol mod: u.adverbs) {
                newMods.add(getBoundSymbol(bindings, mod));
            }    
            retUtt.adverbs = newMods; 
        }

        if(u.words != null) {
            retUtt.words = u.words;
        }

        return retUtt;
    }

    ///////////////////////////////////////////////////////////
    // Determine binding methods
    ///////////////////////////////////////////////////////////

    public static boolean termEquals(Term t1, Term t2) {
        return getTermBindings(t1,t2) != null;
    }

    public static boolean compareTermStruct(Term t1, Term t2) {
        return (t1.getName().equals(t2.getName()) && t1.getArgs().size() == t2.getArgs().size());
    }

    public static Map<Variable, Symbol> getSymbolBindings(Symbol s1, Symbol s2) {
        Map<Variable,Symbol> bindings = new HashMap<Variable,Symbol>();
        return getBindingsSymHelper(bindings, s1, s2);
    }

    public static Map<Variable, Symbol> getBindingsSymHelper(Map<Variable, Symbol> bindings, Symbol s1, Symbol s2) {
        if(bindings == null) return null;
        if(s1 instanceof Term && s2 instanceof Term) {
            return getBindingsTermHelper(bindings, (Term)s1, (Term)s2);
        } else if(s1 instanceof Variable && s2 instanceof Symbol) {
            Symbol tmp = bindings.get(s1);
            if(tmp == null) bindings.put((Variable)s1,s2);
            else if(!tmp.equals(s2)) return null;
 
        } else if(s1.getName().equals("_")) {
            return bindings;
        } else if(!s1.equals(s2)) {
            return null;
        }
        return bindings; 
    }

    public static Map<Variable, Symbol> getTermBindings(Term t1, Term t2) {
        Map<Variable,Symbol> bindings = new HashMap<Variable,Symbol>();
        return getBindingsTermHelper(bindings, t1, t2);
    }
    //------------------------------------------------
    //------------------------------------------------
    public static boolean isLong(String str) {
        try{ Long.parseLong(str); }catch(Exception e){return false;}
        return true;
    }
    //------------------------------------------------
    //------------------------------------------------
    private static boolean namesMatch(String s1, String s2) throws Exception{
        boolean s1ref = s1.contains("_") && isLong(s1.split("_")[1]);
        boolean s2ref = s1.contains("_") && isLong(s1.split("_")[1]);
        
        if(s1ref == s2ref) return (s1.equals(s2));
        if(s1ref) return s1.split("_")[1].equals(s2);
        /*if(s2ref) (always true) */
        return s2.split("_")[1].equals(s1);
        //throw new Exception("namesMatch logic error");
    }

    public static Map<Variable, Symbol> getTermBindingsVars(Term t1, Term t2){
        //System.out.println("Binding "+t1+" == "+t2);
        Map<Variable,Symbol> bindings = new HashMap<Variable,Symbol>();
        if (!compareTermStruct(t1,t2)) return null;
        //System.out.println("getBindingsTermHelper initial check passed");

        ArrayList<Symbol> t1sym = t1.getArgs();
        ArrayList<Symbol> t2sym = t2.getArgs();     

        for(int i=0; i<t1sym.size(); i++) {
            Symbol s1 = t1sym.get(i);
            Symbol s2 = t2sym.get(i);
            if(s1 instanceof Term && s2 instanceof Term) {
                //            System.out.println("term term");
                //            System.out.println("s1: "+s1);
                //           System.out.println("s2: "+s2);
                Map<Variable, Symbol> newBindings = getBindingsTermHelper(bindings, (Term)s1, (Term)s2); 
                if(newBindings != null) bindings = newBindings;
                else return null;
            }
            else if(s1 instanceof Variable && s2 instanceof Variable) {
                // System.out.println("Comping vars:");
                // System.out.println(s1+","+s2);
                //System.out.println(((Variable)s1).getType());
                //System.out.println(((Variable)s2).getType());
                
                if(!(((Variable)s1).getType().equals(((Variable)s2).getType()) ||
                     (((Variable)s1).getType().equals("")) || 
                     (((Variable)s2).getType().equals("")))) {
                    return null;   
                }
            }
            else if(s1 instanceof Variable && s2 instanceof Symbol) {
                //            System.out.println("var sym");
                // System.out.println("var = " + s1 + "; sym = " + s2);
                Symbol tmp = bindings.get(s1);
                //          System.out.println("tmp = " + tmp);
                //          if(tmp != null) System.out.println("tmp class = " + tmp.getClass() + "; s2 class = " + s2.getClass());
                if(tmp == null) bindings.put((Variable)s1,s2);
                else if(tmp instanceof Term && s2 instanceof Term) {
                    //              System.out.println("AHHHHHHHH");
                    Map<Variable,Symbol> newBindings = getBindingsTermHelper(bindings,(Term)tmp, (Term)s2);
                    if(newBindings != null) { 
                        //              System.out.println("AHHHHHHHH WINBOAT");
                        bindings = newBindings;
                    } else {
                        //	      System.out.println("AHHHHHHHH FAILBOAT");
                        return null;
                    }
                }

                else if(!tmp.equals(s2)){
                    //              System.out.println(tmp+" != "+s2);
                    return null;
                }
            }
            else {
                // System.out.println("sym sym");
                // System.out.println("S1: "+s1);
                // System.out.println("S2: "+s2);
                try{
                    //System.out.println("nm: "+s1+","+s2+"?" +namesMatch(s1.getName(),s2.getName()));

                    assert s2 != null;
                    if(!namesMatch(s1.getName(),s2.getName())) {
                        //              System.out.println(s1+" !!!!!= "+s2);
                        return null;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        //    System.out.println("Bindingz: "+bindings);
        return bindings; 
    }

    public static Map<Variable, Symbol> getBindingsTermHelper(Map<Variable, Symbol> bindings, Term t1, Term t2) {
        if (bindings == null) return null;
        if (!compareTermStruct(t1,t2)) return null;
        //System.out.println("getBindingsTermHelper initial check passed");

        ArrayList<Symbol> t1sym = t1.getArgs();
        ArrayList<Symbol> t2sym = t2.getArgs();     

        for(int i=0; i<t1sym.size(); i++) {
            Symbol s1 = t1sym.get(i);
            Symbol s2 = t2sym.get(i);
            if(s1 instanceof Term && s2 instanceof Term) {
                //            System.out.println("term term");
                //            System.out.println("s1: "+s1);
                //           System.out.println("s2: "+s2);
                Map<Variable, Symbol> newBindings = getBindingsTermHelper(bindings, (Term)s1, (Term)s2); 
                if(newBindings != null) bindings = newBindings;
                else return null;
            }
            else if(s1 instanceof Variable && s2 instanceof Symbol) {
                //            System.out.println("var sym");
                // System.out.println("var = " + s1 + "; sym = " + s2);
                Symbol tmp = bindings.get(s1);
                //          System.out.println("tmp = " + tmp);
                //          if(tmp != null) System.out.println("tmp class = " + tmp.getClass() + "; s2 class = " + s2.getClass());
                if(tmp == null) bindings.put((Variable)s1,s2);
                else if(tmp instanceof Term && s2 instanceof Term) {
                    //              System.out.println("AHHHHHHHH");
                    Map<Variable,Symbol> newBindings = getBindingsTermHelper(bindings,(Term)tmp, (Term)s2);
                    if(newBindings != null) { 
                        //              System.out.println("AHHHHHHHH WINBOAT");
                        bindings = newBindings;
                    } else {
                        //	      System.out.println("AHHHHHHHH FAILBOAT");
                        return null;
                    }
                }
                else if(!tmp.equals(s2)){
                    //              System.out.println(tmp+" != "+s2);
                    return null;
                }
            }
            else {
                // System.out.println("sym sym");
                // System.out.println("S1: "+s1);
                // System.out.println("S2: "+s2);
                if(!s1.equals(s2)) {
                    //              System.out.println(s1+" !!!!!= "+s2);
                    return null;
                }
            }
        }
        //    System.out.println("Bindingz: "+bindings);
        return bindings; 
    }
    // public static Term transformAllToITK(Set<Term> pragMeanings){
    //     Set<Term> newSet = new HashSet<Term>();
    //     for(Term t : pragMeanings) newSet.add(transformToITK(t));
    //     return newSet;
    // }

    public static Term transformToITK(Term pragMeaning){
        ArrayList<Symbol> args = new ArrayList<>();
        for(Symbol s:pragMeaning.getArgs())
            if(s instanceof Variable)
                args.add(new Symbol("itkvariable_"+((Variable)s).getType().toLowerCase()));
            else if(s instanceof Term)
                args.add(transformToITK((Term)s));
            else args.add(s);
        return new Term(pragMeaning.getName(),args);            
    }

    public static Term transformFromITK(Term pragMeaning){
        ArrayList<Symbol> args = new ArrayList<>();
        for(Symbol s:pragMeaning.getArgs())
            if(s instanceof Term)
                args.add(transformFromITK((Term)s));
            else if(s.getName().startsWith("itkvariable"))
                args.add(new Symbol(s.getName().substring(12).toUpperCase()));
            else args.add(s);
        return new Term(pragMeaning.getName(),args);            
    }

    public static Term formPrologQueryTerm(Term t) {
        Set<Variable> varSet = t.getVars();
        Map<Variable,Symbol> binding = new HashMap<Variable,Symbol>();
        for(Variable v: varSet) {
            binding.put(v, new Symbol(v.getType())); 
        }
        return getBoundTerm(binding, t);
    }

    public static Type getType(String str) {
        switch (str) {
            case "Stmt":
                return Type.STATEMENT;
            case "AskYN":
                return Type.QUESTIONYN;
            case "AskWH":
                return Type.QUESTIONWH;
            case "Instruct":
                return Type.INSTRUCT;
            case "Ack":
                return Type.ACK;
            case "ReplyY":
                return Type.REPLYY;
            case "ReplyN":
                return Type.REPLYN;
            case "Accept":
                return Type.CMDACCEPT;
            case "Reject":
                return Type.CMDREJECT;
        }
        return Type.UNKNOWN;
    } 
  
    public static ArrayList<Symbol> parseModifiers(String line) {
        ArrayList<Symbol> retList = new ArrayList<Symbol>();
        int opar = line.indexOf('{');
        int cpar = line.lastIndexOf('}');
        String modStr = line.substring(opar+1,cpar);

        String[] modifiers = modStr.split(",");

        Symbol nextMod; 
        for(String s: modifiers) {
            if(s.indexOf('(') > 0) {
                nextMod = Util.functionPredVars(s);
                retList.add(nextMod);
            } else if(!s.trim().equals("")) {
                nextMod = new Symbol(s);
                retList.add(nextMod);
            }
        }

        return retList;
    }

    ///////////////////////////////////////////////////////////
    // Utterance methods
    //////////////////////////////////////////////////////////

    public static Symbol parseSymbol(String sym) {
        if(sym.length()>0 && sym.charAt(0) > 'Z') return new Symbol(sym);
        else {
            return new Variable("",sym);
        }
    }

    public static Utterance parseUtterance(String line) {
        // System.out.println("PARSING :"+line+"\n\n");
        int opar = line.indexOf('(');
        int cpar = line.lastIndexOf(')');
        int delim = 0;
        String name = line.substring(0, opar);
        String args = line.substring(opar+1, cpar).replaceAll(" ","");

        //System.err.println("parseUtterance - " + args);

        Symbol speaker;
        ArrayList<Symbol> listeners = new ArrayList<Symbol>(); 
        Symbol semantics;
        ArrayList<Symbol> modifiers;

        ArrayList<String> tokens = Util.tokenizeArgs(args);

        assert tokens != null;
        for(String s: tokens) {
            log.debug("Token s: " + s);
            //System.err.println(s);
        }


        speaker = parseSymbol(tokens.get(0));
        listeners.add(parseSymbol(tokens.get(1)));
        if(tokens.get(2).indexOf('(') > 0) semantics = Util.functionPredVars(tokens.get(2));
        else semantics = parseSymbol(tokens.get(2));
        modifiers = parseModifiers(tokens.get(3)); 

        Utterance retUtt = new Utterance(semantics);
        retUtt.type = getType(name);
        retUtt.speaker = speaker;
        retUtt.listener = listeners; 
        retUtt.adverbs = modifiers; 
 
        return retUtt; 
    }

    public static Utterance parseAck(String line) {
        int opar = line.indexOf('(');
        int cpar = line.lastIndexOf(')');
        int delim = 0;
        String name = line.substring(0, opar);
        String args = line.substring(opar+1, cpar);

        //System.err.println("parseUtterance - " + args);

        Symbol speaker;
        ArrayList<Symbol> listeners = new ArrayList<Symbol>(); 
        Symbol semantics;
        ArrayList<String> words = new ArrayList<String>();
    
        ArrayList<String> tokens = Util.tokenizeArgs(args);

        assert tokens != null;
        speaker = parseSymbol(tokens.get(0));
        listeners.add(parseSymbol(tokens.get(1)));
        if(tokens.get(2).indexOf('(') > 0) semantics = Util.functionPredVars(tokens.get(2));
        else semantics = parseSymbol(tokens.get(2));

        String txt = tokens.get(3).replaceAll("\"","");
        String[] wordArray = txt.trim().split(" ");
    
        for(String word: wordArray) {
            log.debug("parseAck word " + word);
            words.add(word);
        }

        log.debug("parseAck words " + words );

        Utterance retUtt = new Utterance(words);
        retUtt.type = getType(name);
        retUtt.speaker = speaker;
        retUtt.listener = listeners; 
        retUtt.semantics = semantics;
        return retUtt; 
    
    }
    public static Map<Variable, Symbol> getUtteranceBindings(Map<Variable,Symbol> bindings, Utterance u1, Utterance u2) {

        // 0. check type
        if(u1.type != u2.type) return null;
	
        // 1. check speaker
        //System.err.println("getUtteranceBindings - " + bindings );
        bindings = getBindingsSymHelper(bindings, u1.speaker, u2.speaker);    

        // 2. check listeners - only check first listener for now
        //System.err.println("getUtteranceBindings - " + bindings );
        bindings = getBindingsSymHelper(bindings, u1.listener.get(0), u2.listener.get(0));   

        // 3. check semantics 
        //System.err.println("getUtteranceBindings - " + bindings );
        bindings = getBindingsSymHelper(bindings, u1.semantics, u2.semantics);    

        // 4. check modifiers
        //System.err.println("getUtteranceBindings - " + bindings );
        //System.err.println(u1.adverbs.size() + " " + u2.adverbs.size() );
        if(u1.adverbs.size() != u2.adverbs.size()) return null;
        else {
            for(int i=0; i<u1.adverbs.size(); i++) {
                bindings = getBindingsSymHelper(bindings, u1.adverbs.get(i), u2.adverbs.get(i));    
            }
        }
        //System.err.println("getUtteranceBindings - " + bindings );
        return bindings; 
    }
   
    public static Map<Variable, Symbol> getUtteranceBindings(Utterance u1, Utterance u2) {
        Map<Variable, Symbol> bindings = new HashMap<Variable, Symbol>();
        return getUtteranceBindings(bindings, u1, u2);
    }

 
    /////////////////////////////////////////////////////////////////

    public static boolean compareVars(Set<Variable> v1, Set<Variable> v2) {
        return false;	
    }

}
