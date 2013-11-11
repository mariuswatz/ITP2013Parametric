class Particle {
  float x,y,speed,vol;
  int bufIndex;
  int col;
  
  Particle(int index) {
    bufIndex=index;
    x=random(width);
    y=random(height-waveH);
    speed=random(0.2f,1);
    col=color(random(50,255));
  }
  
  void draw() {
    float damper=0.8;

    // calculate volume using a damping factor    
    vol=vol*damper+(1.0-damper)*(bufL[bufIndex]*50+2);

    y=y-speed;
    if(y<0) y+=height-waveH;

    fill(col);
    ellipse(x,y, vol,vol);
  }
}
  
