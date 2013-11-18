

import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertexList pts;
UGeo geo;
UNav3D nav;
boolean doSave=false;

int drawStyle;

void setup() {
  size(1000, 1000, OPENGL);
  UMB.setPApplet(this);
  nav=new UNav3D();

  build();
}

void draw() {
  background(0);

  // apply custom lighting
  customLights();

  pushMatrix();
  translate(width/2, height/2);
  nav.doTransforms();

  //  for(UVertex v:pts) UMB.pellipse(v, 10);

  if (drawStyle==1) {
    noStroke();
    for (UFace f:geo.getF()) {
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
    stroke(255);
    geo.draw();
  }
  
  popMatrix();
  
  if(doSave) {
    String filename=UFile.nextFile(sketchPath, this.getClass().getSimpleName(), "png");
    saveFrame(filename);
    doSave=false;
  }

  drawCredit();

}

void keyPressed() {
  key=Character.toLowerCase(key );
  
  if (key==TAB) drawStyle=(drawStyle+1)%2;
  else if(key=='d') subdivide();  
  else if (key=='s') doSave=true;
  else if (key=='c') applyColor();
  else if (key=='l') buildLights();
  else if (key==' ') {
    build();
  }
}

void build() {
  int n=(int)random(100, 800);


  pts=new UVertexList();

  float w=width/2;
  float m=random(5, 11)/w;

  while (pts.size ()<n) {
    UVertex pt=new UVertex(random(w), 0).rot(random(TWO_PI));
    pt.z=map(noise(pt.x*m, pt.y*m), 0, 1, -w*0.5f, w*0.5);

    pts.add(pt);
  }

  geo=new UGeo().triangulation(pts).center();
  println(m+" "+n+" "+pts.size()+" "+geo.sizeF());

  applyColor();  
  buildLights();
}
