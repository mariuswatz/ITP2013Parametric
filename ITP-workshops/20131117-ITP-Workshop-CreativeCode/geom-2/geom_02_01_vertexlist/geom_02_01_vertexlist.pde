import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertexList vl,vl2;
UNav3D nav;

void setup() {
  size(600,600, OPENGL);
  
  // initialize UMB with a reference to this sketch
  UMB.setPApplet(this);

  nav=new UNav3D();
    
  build();  
  
}

void draw() {
  background(0);
  
  translate(width/2,height/2);
  nav.doTransforms();
  
  noFill();
  stroke(255,255,0);
  vl.draw();
  
  stroke(0,255,255);
  vl2.draw();
}

void build() {
  vl=new UVertexList();
  
  int n=36;
  for(int i=0; i<n; i++) {
    float a=map(i, 0,n, 0,TWO_PI);
    vl.add(new UVertex(125,0).rotZ(a));
  }
 
  // adds a copy of the first vertex to the end of the list
  vl.close();  
  vl2=vl.copy().scale(2); 
}








