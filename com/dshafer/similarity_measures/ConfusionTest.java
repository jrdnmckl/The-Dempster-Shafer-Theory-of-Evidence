package com.dshafer.similarity_measures;
import com.dshafer.*;
import com.dshafer.DSFrame;
import org.junit.*;
import org.junit.Assert.*;
import java.util.Collections;
import static org.junit.matchers.JUnitMatchers.hasItems;
import java.util.Arrays;

public class ConfusionTest {
  
  @Test
  // Unit test that tests we get list of strings and not NULL
  public void testAssertThathasItemsContainsString() {
    
    double alpha = 0.9;
    double beta = 0.1;
    double alpha2 = 0.6;
    double beta2 = 0.4;

    double coefficient;
    SimilarityMeasure sm = new Confusion();

    DSFrame frame = new DSFrame();
    DSFrame frame2 = new DSFrame();

    frame.constructFrame("Z", "Y");
    frame.clearMasses();
    frame.setMass(alpha, "Z");
    frame.setMass(beta, "Y");
    frame.setMass((beta-alpha), "Z", "Y");
    frame.normalizeMasses();

    frame2.constructFrame("X", "Y");
    frame2.clearMasses();
    frame2.setMass(alpha2, "X");
    frame2.setMass(beta2, "Y");
    frame2.setMass((beta2-alpha2), "X", "Y");
    frame2.normalizeMasses();

    coefficient = sm.computeSimilarity(frame, frame2);
    Assert.assertThat(frame.getHypotheses(), hasItems("Z", "Y"));
    Assert.assertThat(frame2.getHypotheses(), hasItems("X", "Y"));
    System.out.println(frame);
    System.out.println(frame2);

  } 
}



