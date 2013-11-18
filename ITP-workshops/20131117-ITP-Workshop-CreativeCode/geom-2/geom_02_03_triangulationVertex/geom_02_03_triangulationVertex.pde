import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertexList pts;
UGeo geo;
UNav3D nav;

void setup() {
  size(1000,1000,OPENGL);
  UMB.setPApplet(this);
  nav=new UNav3D();
  
  build();
}

void draw() {
  background(0);
  fill(255);
  text(this.getClass().getSimpleName(), 5,15);
  
  translate(width/2,height/2);
  lights();
  
  nav.doTransforms();
  
//  for(UVertex v:pts) UMB.pellipse(v, 10);
  
  stroke(255);
  if(mousePressed) {
    noStroke();
    for(UFace f:geo.getF()) {
      UVertex v[]=f.getV();
      beginShape(TRIANGLE);

      fill(v[0].col);
      UMB.pvertex(v[0]);
      fill(v[1].col);
      UMB.pvertex(v[1]);
      fill(v[2].col);
      UMB.pvertex(v[2]);
      
      endShape();
    }
    
  }
  else {
    
    geo.draw();
  }
}

void keyPressed() {
  if(key!=CODED) {
    build();
  }
  if(key=='s') {
    String filename=UFile.nextFile(sketchPath,this.getClass().getSimpleName(),"png");
    saveFrame(filename);
  }
}

void build() {
  int n=(int)random(100,800);
  buildColor();
  
  pts=new UVertexList();
  
  float w=width/2;
  float m=random(5,11)/w;
  
  while(pts.size()<n) {
    UVertex pt=new UVertex(random(w),0).rot(random(TWO_PI));
    pt.z=map(noise(pt.x*m,pt.y*m), 0,1,-w*0.5f,w*0.5);
    
    pts.add(pt);
  }
  
  geo=new UGeo().triangulation(pts).center();
  println(m+" "+n+" "+pts.size()+" "+geo.sizeF());
  
  
  // set random face colors
  for(UFace f : geo.getF()) f.setColor(randomColor());
  geo.enable(UMB.COLORFACE);

  // assign colors to vertices
  for(UVertex v : geo.getV()) v.setColor(randomColor());
  
}



