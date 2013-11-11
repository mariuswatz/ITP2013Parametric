int bufSize;
float bufL[],bufR[];
float volMax=0, volR, volL;

void processSound() {
  if(bufL==null) {
    bufSize=in.bufferSize();
    bufL=new float[bufSize];
    bufR=new float[bufSize];
  }
  
  for(int i=0; i<bufSize; i++) {
    bufL[i]=in.left.get(i);
    bufR[i]=in.left.get(i);
  }
  
  // calculate levels
  volL=in.left.level();
  volR=in.right.level();
  if(frameCount<5) volMax=0;
  volMax=max(volL, volMax);
  volMax=max(volR, volMax);
}

void drawWaveForm(float theHeight) {
  float y1=(float)theHeight*0.25f;
  float y2=(float)theHeight*0.75f;
  float h=(float)theHeight*0.25f;

  pushMatrix();
  translate(0,height-theHeight);
  
  stroke(100);
  if(theHeight<height)   line(0, 0, width, 0);

  line(0, theHeight/2, width, theHeight/2);
  line(0, y1, width, y1);
  line(0, y2, width, y2);

  noStroke();

  // draw bars to indicate volume
  fill(255, 255,0, 200);
  
  h=volL*theHeight;
  rect(width-30, height-h, 8, h);    

  h=volR*theHeight;
  rect(width-20, height-h, 8, h);    
  
  fill(255, 0, 0, 200);
  h=volMax*theHeight;
  rect(width-10, height-h, 8, h);    

  // draw the waveforms
  fill(255, 200, 0);
  for (int i = 0; i < in.bufferSize() - 1; i++) {
    float l=in.left.get(i)*h;
    float r=in.right.get(i)*h;

    rect(i, y1, 1, l);
    rect(i, y2, 1, r);
  }
  
  popMatrix();
}
