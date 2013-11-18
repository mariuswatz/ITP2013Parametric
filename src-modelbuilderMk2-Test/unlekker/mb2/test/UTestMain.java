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
  String path,saveFilename;
  ArrayList<UTest> tests;
  
  UTest theTest;
  UNav3D nav;
  
  public void setup() {
    size(600,600, OPENGL);
    UTest.p=this;
    UTest.main=this;
    
    UMB.setPApplet(this);
    sketchPath=UFile.getCurrentDir();
    
    tests=new ArrayList<UTest>();

    tests.add(new UTestPrimitives02());
    
    tests.add(new UTestSelector02());
    tests.add(new UTestNormal()); 
    tests.add(new UTestBPatch());
    tests.add(new UTestSelector());
    tests.add(new UTestEdgeList02());
    
//  tests.add(new UTestSTL()); 
    tests.add(new UTestPrimitives());

    tests.add(new UTestTriangulate());
//    tests.add(new UTestSmooth02());
//    tests.add(new UTestSTLColor()); 

    tests.add(new UTestSmooth());
    
//    tests.add(new UTestIntersectionCirc());
//    tests.add(new UTestHeading03());
//    tests.add(new UTestIntersection());
//    tests.add(new UTestHeading02());
//    tests.add(new UTestHeading01());
//    tests.add(new UTestCurve());
//    tests.add(new UTestResample());
//    tests.add(new UTest2D()); 
    
    
    int id=0;
    
    try {
      String dat[]=loadStrings("UTestMain.dat");
      id=Integer.parseInt(dat[0]);
    } catch (Exception e1) {
    }
    theTest=tests.get(id);
    
    try {
      theTest.init();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      exit();
    }
  }

  public void draw() {
    background(0);
     color(255,255,255);
    drawCredit();
    
    pushStyle();
    theTest.draw();
    popStyle();
    
  }

  public void drawGrid() {
    int n=width/100;
    for(int i=1; i<n;i++) {
      
      line(i*100,0,i*100,height);
      line(0,i*100,width,i*100);
    }
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
      saveFilename=filename;
    }
    else if(key==' ' || key=='+' ) {
      int id=(tests.indexOf(theTest)+1)%tests.size();
      theTest=tests.get(id);
      theTest.init();
    }
    else if(key==' ' || key=='-' ) {
      if(keyEvent.isShiftDown() || key=='-') {
        int id=tests.indexOf(theTest);
        id=(id<1 ? id=tests.size()-1 : id-1);
        
        theTest=tests.get(id);
        theTest.init();
        
      }
    }
    else if(key!=CODED && key=='n') {
//      if(keyCode==java.awt.event.KeyEvent.VK_N)     
        theTest.init();
    }

    theTest.keyPressed(key);

    saveStrings("UTestMain.dat",
        new String [] {""+tests.indexOf(theTest)});
  }

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "unlekker.mb2.test.UTestMain" });
  }

  
}
