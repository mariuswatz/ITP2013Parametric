/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UFile;

public class UTestMain extends PApplet {
  String path;
  ArrayList<UTest> tests;
  
  UTest theTest;
  UNav3D nav;
  
  public void setup() {
    size(600,600, OPENGL);
    UTest.p=this;
    UTest.main=this;
    
    UMB.setPApplet(this);
    
    tests=new ArrayList<UTest>();
    
    
    tests.add(new UTestEdgeList());
    tests.add(new UTestHeading03());
    tests.add(new UTestIntersection());
    tests.add(new UTestHeading02());
    tests.add(new UTestHeading01());
//    tests.add(new UTestSTL()); 
    tests.add(new UTestCurve());
    tests.add(new UTestPrimitives());
    tests.add(new UTestResample());
    tests.add(new UTestTriangulate());
//    tests.add(new UTest2D()); 
    
    theTest=tests.get(0);
    theTest.init();
  }

  public void draw() {
    background(0);
    fill(255); 
    drawCredit();
    
    pushStyle();
    theTest.draw();
    popStyle();
    
  }
  
  void drawCredit() {
    if(frameCount<2)   textFont(createFont("courier", 11, false));

    fill(255);
    textAlign(RIGHT);
    text(UMB.version(), width-5, 15);
    textAlign(LEFT);
    text(theTest.getClass().getSimpleName(), 5, 15);
  }
  public void keyPressed() {
    if(key=='s') {
      String filename=UFile.nextFile(sketchPath, theTest.getClass().getSimpleName(), "png");
      println(filename);
      saveFrame(filename);
    }
    else if(key==' ') {
      int id=(tests.indexOf(theTest)+1)%tests.size();
      theTest=tests.get(id);
      theTest.init();
    }
    else if(key!=CODED) {
//      if(keyCode==java.awt.event.KeyEvent.VK_N)     
        theTest.init();
    }

  }

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "unlekker.mb2.test.UTestMain" });
  }

  
}
