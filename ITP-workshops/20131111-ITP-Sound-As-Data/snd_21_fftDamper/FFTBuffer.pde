class FFTBuffer {
  PApplet p;

  FFT fft;
  int specN, avgN;
  Envelope env;
  float maxDamper=-1;
  float damper=-1;

  DataBuffer spectrum, avg;
  float tmpSpec[], tmpAvg[];

  boolean useAverages=false;

  FFTBuffer(PApplet papplet, int bufSize, float sampleRate) {
    p=papplet;

    fft=new FFT(bufSize, sampleRate);      
    specN=fft.specSize();
    spectrum=new DataBuffer(specN);
    tmpSpec=new float[specN];

    initAverages(44, 20);
    setDamper(damper, maxDamper);

    env=new Envelope(specN);
  }

  void initAverages(int minBandwidth, int bandsPerOctave) {
    fft.logAverages(minBandwidth, bandsPerOctave);
    avgN=fft.avgSize();

    avg=new DataBuffer(avgN);
    tmpAvg=new float[avgN];
    p.println("avgSize "+avgN);
  }

  void draw(float h, float y) {
    p.pushMatrix(); // save world matrix

      p.translate(0, y);

    p.fill(255, 100, 0);
    p.noStroke();

    p.fill(255, 255, 0);      
    p.textAlign(p.LEFT);
    p.text("FFT: "+
      "spectrum ["+specN+" "+p.nf(spectrum.max, 1, 2)+"]", 2, -2);
    h--;
    spectrum.draw(p, h);

    p.pushMatrix(); // save world matrix
    p.translate(p.width-avg.n-10, 0);

    p.fill(255, 255, 0);      
    p.text("logAvg ["+avgN+" "+p.nf(avg.max, 1, 2)+"] ", 2, -2);

    p.fill(255, 0, 0);
    avg.draw(p, h);

    p.popMatrix(); // restore world matrix

      p.popMatrix(); // restore world matrix
  }

  void update(AudioBuffer mix, float volume) {
    fft.forward(mix);

    for (int i=0; i<specN; i++) tmpSpec[i]=fft.getBand(i);
    for (int i=0; i<avgN; i++) tmpAvg[i]=fft.getAvg(i);

    spectrum.update(tmpSpec);
    avg.update(tmpAvg);
  }

  void setDamper(float valDamp, float maxDamp) {
    maxDamper=maxDamp;
    damper=valDamp;

    spectrum.setDamper(valDamp, maxDamp);
    avg.setDamper(valDamp, maxDamp);
  }


}

  class Envelope {
    boolean enabled;
    float mult[];

    Envelope(int fftsize) {
      mult=new float[fftsize];
      for (int i=0; i<mult.length; i++) {
        mult[i]=bezierPoint(
        0.05f, 0.1f, 0.2f, 1, 
        map(i, 0, mult.length-1, 0, 1));
      }
    }


    void apply(float in[]) {
      if (enabled) {
        for (int i=0; i<in.length; i++) in[i]=in[i]*mult[i];
      }
    }
  }
