
float x,y;
float xGoal,yGoal;

void setup() {
  size(600,600);
  
  xGoal=width/2;
  yGoal=height/2;
  x=xGoal;
  y=yGoal;
}

void draw() {
  background(0);
  
  ellipse(x,y, 50,50); 
  
  x=xGoal;
  y=yGoal;
}

void mousePressed() {
  xGoal=mouseX;
  yGoal=mouseY;
}
  
  
  
  
  
  
  
  
  
  
