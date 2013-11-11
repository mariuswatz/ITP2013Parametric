
class FFTBuffer {
  FFT fft;
  int fftsize;
  Envelope env;
  float unprocessed[],spectrum[];

  FFTBuffer(int bufSize,float sampleRate) {
    fft=new FFT(bufSize,sampleRate);
    fft.logAverages(88, 20);
    fftsize=fft.avgSize();
    println("avgSize "+fftsize);
    
    spectrum=new float[fftsize];
    unprocessed=new float[fftsize];
    env=new Envelope(fftsize);
    
    damper=0.1;
  }
  
  void draw() {
    pushMatrix(); // save world matrix
    
    translate(200,0);
    
    fill(255);
    for(int i=0; i<fftsize; i++) {
      rect(i,0, 1,spectrum[i]);
    }
    
    translate(fftsize+20,0);
//    env.draw();
    
    popMatrix(); // restore world matrix
  }
  
  void update(AudioBuffer mix) {
    fft.forward(mix);
    for(int i=0; i<fftsize; i++) {
      unprocessed[i]=fft.getAvg(i)*volume;
    }
    env.apply(unprocessed);
    
    for(int i=0; i<fftsize; i++) {
      spectrum[i]=unprocessed[i]*damper+spectrum[i]*(1-damper);
    }
  }
}
