
float x1,y1, x2,y2;
float r1,r2;
float t; // t==time

void setup() {
  size(600,600);
  
  x1=random(width);
  y1=random(height);
  r1=random(10,80);
  
  x2=random(width);
  y2=random(height);
  r2=random(10,80);
}

void draw() {

  background(0);
  
  // calculate t so that it goes [0..1]
  // for every 200 frames
  t=(float)(frameCount % 200)/200;
  text("t="+t, 5, 15);
  
  // linear interpolation
  // (delta)*t+min value / (goal-start)*t+start
  float theX=(x2-x1)*t+x1;
  
  float theY=lerp(y1,y2,t);
  float theR=lerp(r1,r2,t);
  
  ellipse(theX,theY, theR,theR);
}


