import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertexList vl,vl2;
UGeo fan,quadstrip;

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
  
  lights();
  translate(width/2,height/2);
  nav.doTransforms();
  
  noFill();
  
  fill(200,100,0);
  stroke(255,255,0);
  fan.draw();
  
  fill(0,100,200);
  stroke(0,255,255);
  quadstrip.draw();
  quadstrip.drawNormals(30);
}

void build() {
  vl=new UVertexList();
  
  int n=60;
  
  float m=random(50,150);
  float perc=0.3;
  
  for(int i=0; i<n; i++) {
    float a=map(i, 0,n, 0,TWO_PI);
    
    m=m*(1-perc)+random(50,150)*perc;
    
    vl.add(new UVertex(m,0).rotZ(a));
  }
  
  // set the radius of the last vertex
  // to the average of the first and last vertices 
  m=vl.first().mag()+vl.last().mag();
  vl.last().norm(m*0.5);
 
  // adds a copy of the first vertex to the end of the list
  vl.close();  
  
  // smooth list using a corner cutting strategy
  vl=UVertexList.smooth(vl,1);
  
  vl2=vl.copy().scale(2).translate(0,0,100);
  
  fan=new UGeo().triangleFan(vl);
  quadstrip=new UGeo().quadstrip(vl, vl2);
}

void keyPressed() {
  build();
}






