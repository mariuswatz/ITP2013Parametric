
int bufSize=1024;
float bufL[],bufR[];
float volMax,volR,volL;

void processSound() {
  if(bufL==null) { // initialize buffers
    bufL=new float[bufSize];
    bufR=new float[bufSize];
  }
  
  // calculate levels
  volL=input.left.level();  
  volR=input.right.level();
  volMax=max(volMax, volL);
  volMax=max(volMax, volR);
  
  for(int i=0; i<bufSize; i++) {
    bufL[i]=input.left.get(i);
    bufR[i]=input.right.get(i);
  }
}



void drawWaveForm() {
  float y1=(float)height*0.25;
  float y2=(float)height*0.75;
  float h=(float)height*0.25;  
  
  stroke(255);
  line(0,y1, width,y1);
  line(0,y2, width,y2);
  line(0,height/2, width,height/2);
  
  noStroke();
  fill(255,255,0);
  
  for(int i=0; i<bufSize; i++) {
    float val=bufL[i]*h; // left buffer
    rect(i,y1, 1,val);
    
    val=bufR[i]*h; // right buffer
    rect(i,y2, 1,val);
  }
  
  // draw volume bars
  fill(0,255,255);
  rect(5, height, 5, -volMax*h);
  rect(15, height, 5, -volL*h);
  rect(25, height, 5, -volR*h);   
}








