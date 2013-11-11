FFT fft;
int bufSizeFFT;
float bufFFT[],bufFFTtmp[];
float volFFT;

void initFFT() {
  fft=new FFT(bufSize, input.sampleRate());
  bufSizeFFT=fft.specSize();
  println("FFT bufSize: "+bufSizeFFT);
  
  bufFFT=new float[bufSizeFFT];
  bufFFTtmp=new float[bufSizeFFT];
}

void processFFT() {
  fft.forward(input.mix);
  
  // get the current values and store them in bufFFTtmp
  for(int i=0; i<bufSizeFFT; i++) {
    bufFFTtmp[i]=fft.getBand(i);
  }  
  
  float volNow=max(bufFFTtmp);
  
  // apply damping to find the new volume
  volFFT=volFFT*(1-damper)+volNow*damper;
  volFFT=max(0.1,volFFT);
  
  // calculate the new normalized values
  for(int i=0; i<bufSizeFFT; i++) {
    float newVal=min(1, bufFFTtmp[i]/volFFT);
    
    if(doShaper) newVal=shaper(newVal);
    
    bufFFT[i]=bufFFT[i]*(1-damper)+newVal*damper;
  }
}

// function to shape an input value to emphasize different
// parts of the [0..1] range
float shaper(float t) {
  float val;
  
  val=t*t;
  val=min(1, bezierPoint(0.1,0.1, 1,1, t));
  
  return val;
}


void drawFFT() {
  fill(255,255,0);
  noStroke();
  
  for(int i=0; i<bufSizeFFT; i++) {
    rect(i,height, 1,-bufFFT[i]*500);
  }
}
