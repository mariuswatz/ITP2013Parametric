import unlekker.mb2.test.*;
import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

import processing.opengl.*;

UVertexList vl;

void setup() {
  size(600,600,OPENGL);
  
  UGeo.setGraphics(this);
  
    vl=new UVertexList();
    vl.add(0,0,0);
    vl.add(100,0,0);
    vl.add(100,100,0);
    vl.add(0,100,0);
    vl.add(0,0,0);

}

void draw() {
  background(0);
  
  stroke(255);
  noFill();
  
  vl.draw();
}
