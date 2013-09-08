package unlekker.mb2.test;

import java.util.ArrayList;

import processing.core.PApplet;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UGeoIO;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UFile;

public class UGeoTest extends PApplet {
  ArrayList<UVertexList> vvl;
  
  UVertexList vl,vl2;
  UGeo geo;
  
  public void setup() {
    size(600,600, OPENGL);
    
    vvl=new ArrayList<UVertexList>();
    
    UVertexList.setGraphics(g);
    vl=new UVertexList();
    vl.add(0,0,0);
    vl.add(100,0,0);
    vl.add(100,100,0);
    vl.add(0,100,0);
    vl.add(0,0,0);
    
    // vertices were added clock-wise, so reverse
    vl.reverse(); 
    
    vl.translate(-50, -150, 0);
    for(int i=0; i<30; i++) {
      // chained commands:
      // #1 copy vertex list
      // #2 rotate and translate copy
      // #3 add copy to vvl
      // Remember: The original "vl" is left unchanged
      vvl.add(vl.copy().rotX(radians(10*i)).translate(0, 0, 20));
      
      println(vl.str());
    }

    geo=new UGeo();
    
    // make quadstrips of all vertex lists in vvl
    geo.quadstrip(vvl);
    
    // fill first and last vertex lists w/ triangle fans
    geo.triangleFan(vvl.get(vvl.size()-1));
    
    // the first fan needs to have its order reversed to face
    // the right way
    geo.triangleFan(vvl.get(0),true);
    
    
    // randomize face colors
    int id=0,n=geo.getFaces().size();           
    for(UFace f:geo.getFaces()) {
      float v=map(id++,0,n-1,0,255);
      f.setColor(v*255,v*100,0);
    }

    geo.enable(geo.COLORFACE); // per-face coloring
    println(geo.optionStr()); // print options string
    
    // write STL using incremental file naming
    // "data/" is the root dir, "test" is the file prefix
    UGeoIO.writeSTL(UFile.nextFilename("data\\", "test"), geo);
  }

  public void draw() {
    background(0);
    
    translate(width/2,height/2);
    lights();
    
    float ry=map(width/2-mouseX,-width/2,width/2, PI,-PI);
    float rx=map(height/2-mouseY,-height/2,height/2, PI,-PI);
    
    rotateY(ry+radians(frameCount));
    rotateX(rx);
    
    
    stroke(255);
    fill(255);
    
    geo.draw();
  }

  static public void main(String args[]) {
    String sk[]=new String[] {
        "UGeoTest",
        "UFileTest"
    };
    
    PApplet.main(new String[] {"unlekker.mb2.test."+sk[0]});
  }

  
}
