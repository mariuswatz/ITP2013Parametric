import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertexList pts;
UGeo geo;
UNav3D nav;

void setup() {
  size(600,600,OPENGL);
  UMB.setPApplet(this);
  nav=new UNav3D();
  
  build();
}

void draw() {
  background(0);
  translate(width/2,height/2);
  lights();
  nav.doTransforms();
  
  for(UVertex v:pts) UMB.pellipse(v, 10);
  
  stroke(255);
  geo.draw();
}

void build() {
  int n=150;
  buildColor();
  
  pts=new UVertexList();
  while(pts.size()<n) {
    UVertex pt=new UVertex(random(50,300),0);
    pt.z=random(-100,100);
    pts.add(pt.rot(random(TWO_PI)));
  }
  
  
  geo=new UGeo().triangulation(pts);
  for(UFace f : geo.getF()) f.setColor(randomColor());
  
  geo.enable(UMB.COLORFACE);
}



