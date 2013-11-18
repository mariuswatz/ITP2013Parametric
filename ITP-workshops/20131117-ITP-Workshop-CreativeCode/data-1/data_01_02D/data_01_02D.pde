
float val[];

void setup() {
  size(600, 600);

  val=new float[100];
  for (int i=0; i<val.length; i++) {
    val[i]=random(100);
  }
}

void draw() {
  background(0);

  noFill();

  pushMatrix();  
  translate(20, 120);
  drawValues(val, width-40, 100);
  popMatrix();

  translate(width/2, height-250);
  drawValuesRadial(val, 40, 200);
}

void drawValues(float dat[], float w, float h) {
  // find the maximum value in the dat array
  float maxVal=max(dat);
  int n=dat.length;

  stroke(100);

  line(0, 0, w, 0);
  line(0, -h, w, -h);

  stroke(255);

  beginShape();
  for (int i=0; i<n; i++) {
    float x=map(i, 0, n-1, 0, w);

    // map y to [0..-h] so that it draws the
    // graph "upwards"
    float y=map(dat[i], 0, maxVal, 0, -h);

    vertex(x, y);
  }
  endShape();
}

void drawValuesRadial(float dat[], float rInner, float rOuter) {
  float maxVal=max(dat);
  int n=dat.length;

  stroke(100);
  ellipse(0, 0, rInner*2, rInner*2);
  ellipse(0, 0, rOuter*2, rOuter*2);

  stroke(255, 255, 0);

  float x, y, a, r;
  beginShape();
  for (int i=0; i<n; i++) {
    // angle + radius == polar coordinates
    a=map(i, 0, n, 0, radians(360));
    r=map(dat[i], 0, maxVal, rInner, rOuter);

    x=cos(a)*r;
    y=sin(a)*r;
    vertex(x, y);

    // multiplier to scale x,y to radius=rInner
    float m=rInner/dist(0, 0, x, y);
    line(x, y, x*m, y*m);
  }
  endShape();
}








