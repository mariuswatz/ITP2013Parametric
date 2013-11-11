
float val;
float newVal;
float damper=0.02;

void setup() {
  size(600,600);
  
}

void draw() {
  background(0);
  
  // dampen value to move towards newVal
  val=val*(1-damper)+newVal*damper;
  ellipse(width/2,height/2, val,val);
  
  keyPressed();
}

void keyPressed() {
  newVal=random(20,400);
`}
