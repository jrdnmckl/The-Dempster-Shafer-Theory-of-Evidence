package com.dshafer.similarity_measures;
import com.dshafer.*;
import com.dshafer.DSFrame;
import org.junit.*;
import org.junit.Assert.*;
import java.util.Collections;
import static org.junit.matchers.JUnitMatchers.hasItems;
import java.util.Arrays;

public class JaccardTest {
  
  @Test
  // Unit test that tests we get list of strings and not NULL
  public void testAssertThathasItemsContainsString() {
     
    double alpha = 0.5;
    double beta = 0.2;
    double gamma = 0.9;
    double delta = 0.4;

    double coefficient;
    SimilarityMeasure sm = new Jaccard();

    DSFrame frame = new DSFrame();
    DSFrame frame2 = new DSFrame();

    frame.constructFrame("Z", "Y");
    frame.clearMasses();
    frame.setMass(alpha, "Z");
    frame.setMass(beta, "Y");
    frame.setMass((alpha - beta), "Z", "Y");
    frame.normalizeMasses();

    frame2.constructFrame("X", "Y");
    frame2.clearMasses();
    frame2.setMass(gamma, "X");
    frame2.setMass(delta, "Y");
    frame2.setMass((gamma - delta), "X", "Y");
    frame2.normalizeMasses();

    coefficient = sm.computeSimilarity(frame, frame2);
    Assert.assertThat(frame.getHypotheses(), hasItems("Y", "Z"));
    Assert.assertThat(frame2.getHypotheses(), hasItems("X", "Y"));
    System.out.println(frame);

  } 
}



