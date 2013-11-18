import unlekker.mb2.geo.*;
import unlekker.mb2.util.*;
import ec.util.*;

UVertex v,vD;
float aD;

void setup() {
  size(600,600);
  
  v=new UVertex(width/2,height/2);
  vD=new UVertex(random(1,3), 0);
  vD.rot(random(TWO_PI));
  
  aD=radians(random(0.5,1.5));
  if(random(100)<50) aD=-aD;
}

void draw() {
  background(0);
  
  // add vD to v
  v.add(vD);
  vD.rot(aD);
  
  ellipse(v.x,v.y, 50,50);
}






