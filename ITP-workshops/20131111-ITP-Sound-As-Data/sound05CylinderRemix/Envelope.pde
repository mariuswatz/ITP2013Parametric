
class Envelope {
  float mult[];
  boolean enabled=true;
  
  Envelope(int fftsize) {
    mult=new float[fftsize];
    for(int i=0; i<mult.length; i++) {
      mult[i]=bezierPoint(
        0.05,0.1, 0.2,1,
        map(i, 0,mult.length-1, 0,1));
    }
  }
  
  void apply(float in[]) {
    if(enabled) {
      for(int i=0; i<in.length; i++) in[i]=in[i]*mult[i];
    }
  }
  
  void draw() {
    noFill();
    
    if(enabled) {
      stroke(0,255,255);
      rect(0,0, mult.length,200);
      
      noStroke();
      fill(0,255,255);
      for(int i=0; i<mult.length; i++) {
        rect(i,0, 1,mult[i]*200);
      }
    }
  }  
}
