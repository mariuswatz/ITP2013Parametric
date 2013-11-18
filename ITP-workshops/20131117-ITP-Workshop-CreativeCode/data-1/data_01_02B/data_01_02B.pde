
float val[];

void setup() {
  size(600,600);
  
  val=new float[100];
  for(int i=0; i<val.length; i++) {
    val[i]=random(100);
  }
  
}

void draw() {
  background(0);
  
  noFill();
  stroke(255);
  
  beginShape();
  for(int i=0; i<val.length; i++) {
    vertex(i,val[i]);
  }
  endShape();
  
}






