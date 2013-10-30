package itp;

import processing.core.PApplet;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;


public class MBTest extends PApplet {
  UVertexList vl;
  UGeo geo;
  UNav3D nav;

  public void setup() {
    size(600, 600, OPENGL);

    // set PApplet reference
    UMB.setPApplet(this);

    // create navigation tool
    nav=new UNav3D();

    build();
  }
  

public void keyPressed() {
  // build if any non-special key is pressed
  if(key!=CODED) build();
}

void drawCredit() {
  fill(255);
  textAlign(RIGHT);
  text(UBase.version(), width-5, 15);
  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
}
  void build() {
    vl=new UVertexList();
    UVertex v=new UVertex(100, 0);
    
    int n=UBase.rndInt(10,50);
    
    for (int i=0; i<n; i++) {
      vl.add(v.copy().rotZ(map(i, 0, n, 0, TWO_PI)).
        mult(vl.rnd(1, 2)).
        add(0, 0, random(-200, 200)));

      if(i>0) {
        // set Z of vertex to 80% of last Z value (if any)
        // and 20% of the new random value 
        vl.get(i).z=(vl.get(i).z*0.3f+vl.get(i-1).z*0.7f);
      }
    }
    
    vl.close();

    // center the vertex list around origin
    vl.center();
    
    // scale the vertex list so that its width is 500
    vl.scale(300f/vl.bb().dimX());

    vl.log("Centroid: "+vl.centroid().str());
    vl.log("UBB: "+vl.bb().str());
    vl.log("Vertices: "+vl.bb.str());

    // create quadstrip mesh from vl and a copy of vl
    // scaled to 20%  
    geo=new UGeo().quadstrip(vl,vl.copy().scale(0.2f));
  }
  public void draw() {
    background(0);
    drawCredit();

    // draw bounding box values
    text(vl.bb().str(), 5, height-5);

    translate(width/2, height/2);
    lights();

    // execute navigation transforms
    nav.doTransforms();

    // draw model
    stroke(255, 255, 0);
    fill(255, 255, 0, 100);
    geo.draw();

    // mark center of sketch
    noFill();
    stroke(255);
    ellipse(0, 0, 5, 5);

    // mark centroid of vl
    vl.pline(vl.centroid(), new UVertex());    
    vl.draw();

    // get and draw bounding box (UBB) of model
    stroke(100, 100, 100, 100);
    geo.bb().draw();
  }
  static public void main(String args[]) {
    PApplet.main(new String[] {
        "itp.MBTest"
    });
  }

}
