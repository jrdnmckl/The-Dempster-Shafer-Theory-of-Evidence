/**
 * Agent Development Environment (ADE)
 * 
 * @version 1.0
 * @author Rehj Cantrell
 * 
 * Copyright 1997-2013 Rehj Cantrell and HRILab (hrilab.org) All rights
 * reserved. Do not copy and use without permission. For questions contact Rehj
 * Cantrell at rcantrel@indiana.edu.
 */
package com.dshafer;


import com.dshafer.*;
import com.Symbol;
import java.util.*;
import java.io.Serializable;
import utilities.Util;

/**
 * An Utterance is used to transmit information between servers, including the
 * semantic meaning, the dialogue act, the speaker, and the listener(s). This is
 * the only NLP-specific class that is used by servers outside of NLP.
 */
public final class Utterance implements Serializable {

  public Type type;

  public Symbol speaker;
  public ArrayList<Symbol> listener;

  public ArrayList<String> words;
  public Symbol semantics;

  public String stringSemantics;
  public ArrayList<String> facts;

  public ArrayList<Symbol> adverbs;

  public void initializeSemantics() {
    semantics = new Symbol("null");
  }

  public void initializeAdverbs() {
    adverbs = new ArrayList<Symbol>();
  }

  public void initializeListener() {
    listener = new ArrayList<Symbol>();
    listener.add(new Symbol("self"));
  }

  public void initializeListener(String listenerName) {
    listener = new ArrayList<Symbol>();
    listener.add(new Symbol(listenerName));
  }

  public void initializeSpeaker(String speaker) {
    if (speaker == null || speaker.length() == 0) {
      this.speaker = new Symbol("commX");
      // This is to hack the fact that in test I don't get a speaker
    } else {
      this.speaker = new Symbol(speaker);
    }
  }

  public Utterance() {
    initializeListener();
    initializeAdverbs();
    initializeSemantics();
  }

  public Utterance(ArrayList<String> words, Type type) {
    this.words = words;
    this.type = type;
    initializeAdverbs();
    initializeSemantics();
  }

  public Utterance(ArrayList<String> words) {
    this.words = words;
    initializeAdverbs();
    initializeSemantics();
  }

  public Utterance(Symbol semantics) {
    this.semantics = semantics;
    initializeAdverbs();
  }

  public Utterance(Symbol semantics, Type type) {
    this.semantics = semantics;
    this.type = type;
    initializeAdverbs();
  }

  public void addAdverb(Symbol adverb) {
    adverbs.add(adverb);
  }

  //////////// string conversions //////////////
  public String toString() {
    String str = "";
    str += (type != null ? type.toString() : "_") + "(";
    str += (speaker != null ? speaker.toString() : "_") + ",";
    str
            += (listener != null && !listener.isEmpty() ? listener.get(0).toString() : "_")
            + ",";
    str += (semantics != null ? semantics.toString() : "_") + ",";
    //str += (stringSemantics!=null?stringSemantics:"_") + ",";

    str += "{";
    String modifiers = "";
    if (facts != null) {
      for (String fact : facts) {
        modifiers += fact + ",";
      }
    }
    if (modifiers.length() > 0) {
      str += modifiers.substring(0, modifiers.length() - 1);
    }
    str += "},";

    str += "{";
    modifiers = "";
    if (adverbs != null) {
      for (Symbol s : adverbs) {
        modifiers += s.toString() + ",";
      }
    }
    if (modifiers.length() > 0) {
      str += modifiers.substring(0, modifiers.length() - 1);
    }
    str += "}";

    str += ")";
    return str;
  }

  /**
   * (cindy;cmdrZ;needs(cmdrZ,med_kit);Statement;{really})
   */
  public static Utterance fromString(String string) {
    String[] parts = string.split(";");
    String type = parts[0];

    String speaker = parts[1].substring(1);
    String listener = parts[2];
    String stringSemantics = parts[3];

    String stringFacts = parts[4];
    stringFacts = stringFacts.substring(1, stringFacts.length() - 1);
    StringTokenizer tokens = new StringTokenizer(stringFacts, " ");
    ArrayList<String> facts = new ArrayList<String>();
    while (tokens.hasMoreTokens()) {
      facts.add(tokens.nextToken());
    }

    String stringAdverbs = parts[5];
    stringAdverbs = stringAdverbs.substring(1, stringAdverbs.length() - 2);
    tokens = new StringTokenizer(stringAdverbs, ",");
    ArrayList<Symbol> adverbs = new ArrayList<Symbol>();
    while (tokens.hasMoreTokens()) {
      adverbs.add(new Symbol(tokens.nextToken()));
    }

    Utterance utterance = new Utterance();

    utterance.speaker = new Symbol(speaker);
    utterance.listener.add(new Symbol(listener));
    utterance.stringSemantics = stringSemantics;
    if (stringSemantics != null && stringSemantics.length() > 0
            && !stringSemantics.equals("_")) {
      utterance.semantics = Util.createSymbol(stringSemantics);
    }
    utterance.type = Type.valueOf(type);
    utterance.facts = facts;
    utterance.adverbs = adverbs;

    return utterance;
  }

  public void setType(String s) {
    this.type = Type.valueOf(s);
  }

  public Type getType() {
    return type;
  }
}
