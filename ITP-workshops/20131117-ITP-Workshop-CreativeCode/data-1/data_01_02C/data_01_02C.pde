
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
  
  translate(50,150);
  drawValues(val, width-100, 100);  
}

void drawValues(float dat[],float w,float h) {
  // find the maximum value in the dat array
  float maxVal=max(dat);
  int n=dat.length;
  
  line(0,0, w,0);
  line(0,-h, w,-h);
  
  beginShape();
  for(int i=0; i<n; i++) {
    float x=map(i, 0,n-1, 0,w);
    
    // map y to [0..-h] so that it draws the
    // graph "upwards"
    float y=map(dat[i], 0,maxVal, 0,-h);
    
    vertex(x,y);
  }
  endShape();
}











