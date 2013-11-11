BeatDetect beat;

float beatVal[];

void initBeat() {
  beat=new BeatDetect();
//  beat.setSensitivity(100); // milliseconds
  beatVal=new float[3];
}

void drawBeat() {
  beat.detect(bufL);

  if(beat.isOnset()) beatVal[0]=100;
  else beatVal[0]*=0.95;

  if(beat.isHat()) beatVal[1]=100;
  else beatVal[1]*=0.95;
  
  if(beat.isSnare()) beatVal[2]=100;
  else beatVal[2]*=0.95;
  
  fill(0, 255, 255);
  for(int i=0; i<3; i++) {
    float x=map(i,0,2, (float)width*0.25, (float)width*0.75);  
    ellipse(x, height/2, beatVal[i]+5, beatVal[i]+5);

  }
}

