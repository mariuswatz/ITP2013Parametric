int bufSize=1024;
float bufL[], bufR[];
float volMax=0, volR, volL;

boolean doNormalize;

public void processSound() {
  // calculate levels
  volL=abs(in.left.level());
  volR=abs(in.right.level());

  // avoid invalid values at start  
  if (frameCount<5) volMax=0;

  volMax=max(volL, volMax);
  volMax=max(volR, volMax);

  // make sure volMax>0
  if (volMax<0.001f) volMax=0.001f;

  if (doNormalize) {
    // get buffer data, normalize values using volMax
    for (int i=0; i<bufSize; i++) {
      bufL[i]=constrain(in.left.get(i)/volMax, -1, 1);
      bufR[i]=constrain(in.right.get(i)/volMax, -1, 1);
    }
  }
  else {
    // get buffer data, don't normalize
    for (int i=0; i<bufSize; i++) {
      bufL[i]=in.left.get(i);
      bufR[i]=in.right.get(i);
    }
  }
}

public void drawWaveForm(float theHeight, float y) {
  float y1=(float)theHeight*0.25f;
  float y2=(float)theHeight*0.75f;
  float h=(float)theHeight*0.25f;

  pushMatrix();
  translate(0, y);

  fill(20, 200);
  rect(0, 0, width, theHeight);

  fill(200);
  textAlign(LEFT);
  text("Volume: "+
    "L "+nf(volL*100, 1, 2)+
    " R "+nf(volL*100, 1, 2)+
    " MAX "+nf(volMax*100, 1, 2), 2, -2);    

  stroke(100);
  if (theHeight<height)   line(0, 0, width, 0);

  line(0, theHeight/2, width, theHeight/2);
  line(0, y1, width, y1);
  line(0, y2, width, y2);

  noStroke();

  // draw bars to indicate volume
  fill(255, 255, 0, 200);

  h=volL*theHeight;
  rect(width-30, theHeight-h, 8, h);    

  h=volR*theHeight;
  rect(width-20, theHeight-h, 8, h);    

  fill(255, 0, 0, 200);
  h=volMax*theHeight;
  rect(width-10, theHeight-h, 8, h);    

  // draw the waveforms
  fill(255, 200, 0);
  h=(float)theHeight*0.25f;

  for (int i = 0; i < in.bufferSize() - 1; i++) {
    float l=bufL[i]*h;
    float r=bufR[i]*h;
    if (abs(l)>h) println(i+" "+l+" "+h+" "+bufL[i]);

    rect(i, y1, 1, l);
    rect(i, y2, 1, r);
  }

  popMatrix();
}
