# Dempster - Shafer

This repo is an extension of the research I've been completing during my senior year Cognitive & Brain Science Seminar at Tufts University. This research focuses on Dempster-Shafer's Evidence Theory and the different similarity measures used to compare two separate bodies of evidence (BoE's). I've implemented this java library for further use in the research and development of embodied cognitive systems. In order to compile these files I've provided, you'll need to have the latest version of Apache Ant installed. 
  - If you're using a Linux machine, open a terminal window and type the following in the command line: "sudo apt-get install ant" and then follow the instructions. 
  - If you're using Unix/Windows, download a binary distribution of ANT [here](http://ant.apache.org/bindownload.cgi), then choose a mirror and download the zip (zip is the easiest method to set). 
  Unzip the file on your computer, then copy the path (traverse until the bin folder). 
    **Next**, you'll need to set up an environment variable. In your bash_profile, Set ANT_HOME = installation path (the path you've just copied) and save. Now execute, ``` echo $ANT_HOME and $JAVA_HOME ``` respectively. You should see the result as your installation path. Before compiling any of the provided source code, you'll need to change your anthome environment in your bash script. 

Include the following in your bash_profile (UNIX) or follow the instructions from this [link](https://docs.oracle.com/cd/E19316-01/820-7054/gicjc/index.html) to change anthome: 
```
export ANT_HOME=/opt/apache-ant-1.9.7
export PATH=$ANT_HOME/bin:$PATH
```

Once ANT is installed and you've changed your Ant Home environment your work is done. Run the command ``` ant dsjar ``` to get a JAR of all the source code included. The JAR should appear in the ** dist/ ** folder in the main directory. After receiving the *DS.jar* file , move to your terminal and compile the source code in the jar by running the following command:

```
ant utilities dshafer
```
If you need to compile any unit tests run the following:
```
ant run-junit-test -Dname=com.dshafer.similarity_measures.NAME_OF_TEST_FILE

EXAMPLE:
ant run-junit-test -Dname=com.dshafer.similarity_measures.CosineTest
```
This will give you feedback and allow you to automate some testing with specific files.

## CODE EXAMPLE
If you wanted to write your own similarity measure, all you really need is to write a class which extends SimilarityMeasure. 
```
public class AwesomeSimMeasure extends SimilarityMeasure {
    public abstract double computeSimilarity(DSFrame s1, DSFrame s2){
    /* important calculations go here */
    }
}
```

In order to determine if your newly created class works like it should, you'll want to create a test class with the same name as your class, plus "Test" at the end as a simple convention. Unit test using any methods you see fit, but note that the DS Frame operator is assumed to operate on vectors of masses...

```
public class AwesomeSimMeasureTest {

@Test
  public void superUsefulTestingMethod(){
    
    double alpha = 0.8;
    double beta = 0.2;
    double gamma = 0.6;
    double delta = 0.4;

    double coefficient;
    SimilarityMeasure sm = new AwesomeSimMeasure();

    DSFrame frame = new DSFrame();
    DSFrame frame2 = new DSFrame();

    frame.constructFrame("AnotherWayofThinking", "SomeOtherBelief");
    frame.clearMasses();
    frame.setMass(alpha, "AnotherWayofThinking");
    frame.setMass(beta, "SomeOtherBelief");
    frame.setMass((alpha - beta), "AnotherWayofThinking", "SomeOtherBelief");
    frame.normalizeMasses();

    frame2.constructFrame("AnIdea", "SomeOtherBelief");
    frame2.clearMasses();
    frame2.setMass(gamma, "AnIdea");
    frame2.setMass(delta, "SomeOtherBelief");
    frame2.setMass((gamma - delta), "AnIdea", "SomeOtherBelief");
    frame2.normalizeMasses();

    coefficient = sm.computeSimilarity(frame, frame2);
    Assert.assertThat(frame.getHypotheses(), hasItems("SomeOtherBelief", "AnotherWayofThinking"));
    Assert.assertThat(frame2.getHypotheses(), hasItems("AnIdea", "SomeOtherBelief"));
    System.out.println(frame);
  }
}




