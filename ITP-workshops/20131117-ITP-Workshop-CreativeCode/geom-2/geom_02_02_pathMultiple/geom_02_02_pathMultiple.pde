import processing.pdf.*;

import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertexList path,vl,vl2,perpl;

ArrayList<UGeo> models;

void setup() {
  size(600,600,OPENGL);
  UMB.setPApplet(this);
  
  models=new ArrayList<UGeo>();
  
  build();
  println(path.str());
}

void draw() {
  background(255);
  translate(width/2,height/2);
  
  noFill();
  stroke(0);
  path.draw();
  
  stroke(255,0,0);
  vl.draw();
  
  stroke(255,0,128);
  vl2.draw();
  
  int id=0;
  for(UVertex vv : path) {
    // perpendicular
    UVertex perp=perpl.get(id);
    line(vv.x,vv.y, vv.x+perp.x,vv.y+perp.y);
    line(vv.x,vv.y, vv.x-perp.x,vv.y-perp.y);
    
    id++;
  }
  
  stroke(0);
  UGeo.drawModels(models);
}

void keyPressed() {
  build();
  
  if(key=='p') savePDF();
}

void build() {
  UVertex v,vD;
  path=new UVertexList();
  vl=new UVertexList();
  vl2=new UVertexList();
  
  perpl=new UVertexList();
  
  v=new UVertex();
  vD=new UVertex(5,0).rot(random(TWO_PI));
  
  float a1=radians(random(1,3));
  float a2=radians(random(1,3));
  
  int n=60;
  for(int i=0; i<n; i++) {
    float a=lerp(a1,a2, map(i, 0,n-1, 0,1));
    v.add(vD.rot(a));
    path.add(v);
  }

  float bez[]=new float[] {0.1,0.8,0.3,0};
    
  for(int i=0; i<n-1; i++) {
    UVertex perp=path.get(i).delta(path.get(i+1));
    
    float t=map(i, 0,n-1, 0,1);
    float m=bezierPoint(bez[0],bez[1],bez[2],bez[3], t);
    
//    m=random(0.1,1);
    
    perp.rot(HALF_PI).norm(50*m);
    perpl.add(perp);
  }
  
  // add a copy of the last perpendicular,
  // which we'll use for both the last and
  // next-to-last
  perpl.add(perpl.last());
  
  int id=0;
  for(UVertex pt : path) {
    UVertex perp=perpl.get(id);

    vl.add(pt.copy().sub(perp));
    vl2.add(pt.copy().add(perp));
    id++;
  }
  
  // creates a UGeo of this model
  UGeo geo=new UGeo().quadstrip(vl,vl2);  

  // add this model to models list
  models.add(geo);
  
}
