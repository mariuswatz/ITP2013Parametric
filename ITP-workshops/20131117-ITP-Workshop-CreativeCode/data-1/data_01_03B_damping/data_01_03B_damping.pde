
float x,y;
float xGoal,yGoal;
float r,rGoal;

void setup() {
  size(600,600);
  
  xGoal=width/2;
  yGoal=height/2;
  x=xGoal;
  y=yGoal;
}

void draw() {
//  background(0);
  
  ellipse(x,y, r,r); 
  
  float perc=0.05;

  // move towards xGoal with "perc" as damping factor  
  x=dampen(x,xGoal, perc);
  y=dampen(y,yGoal, perc);
  r=dampen(r,rGoal, perc);
}

// a=start, b=goal
float dampen(float a,float b,float perc) {
  return a*(1-perc)+b*perc;
}

void mouseMoved() {
  xGoal=mouseX;
  yGoal=mouseY;
  
  // distance between center of screen and mouse pos
  float d=dist(mouseX,mouseY, width/2,height/2);
  rGoal=map(d, 0,width, 20,150);
}
  
  
  
  
  
  
  
  
  
  
