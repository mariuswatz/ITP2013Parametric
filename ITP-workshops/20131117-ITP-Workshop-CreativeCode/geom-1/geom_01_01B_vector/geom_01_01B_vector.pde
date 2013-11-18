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
  
  newRotation();
}

void newRotation() {
  aD=radians(random(0.5,1.5));
  if(random(100)<50) aD=-aD;
  
  // change speed by changing the magnitude of vD
  vD.norm(random(1,3));
}

void draw() {
//  background(0);
  
  // add vD to v
  v.add(vD);
  vD.rot(aD);
  if(v.x>width) v.x=0;
  if(v.x<0) v.x=width;
  if(v.y>height) v.y=0;
  if(v.y<0) v.y=height;
  
  stroke(0);
  ellipse(v.x,v.y, 50,50);
  
  // draw the vD direction vector
  stroke(255,0,0);
  line(v.x,v.y, v.x+vD.x*20,v.y+vD.y*20);
  
  if(frameCount%100==0) newRotation();
}






