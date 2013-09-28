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
import unlekker.mb2.util.UMbMk2;
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
    
    UMbMk2.setPApplet(this);

    tests=new ArrayList<UTest>();
    tests.add(new UTestTriangulate());
    tests.add(new UTest2D());
    
    theTest=tests.get(0);
    theTest.init();
  }

  public void draw() {
    background(0);
    fill(255);
    text(theTest.getClass().getSimpleName(),10,20);
    theTest.draw();
    
    
  }

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "unlekker.mb2.test.UTestMain" });
  }

  
}
