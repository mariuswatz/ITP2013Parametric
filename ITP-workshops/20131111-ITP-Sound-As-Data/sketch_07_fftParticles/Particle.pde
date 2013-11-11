
ArrayList<Particle> particles;

void initParticles() {
  particles=new ArrayList<Particle>();
  
  for(int i=0; i<100; i++) {
    int rndPos=(int)(random((float)bufSizeFFT*0.6));
    particles.add(new Particle(i));
  }
}

class Particle {
  float x,y,speed;
  float val;
  int col;
  int bufIndex;
  
  Particle(int index) {
    bufIndex=index;
    x=map(index, 0,bufSizeFFT, 10,width-10);
    x=random(width);
    
    y=random(height);
    col=color(255, random(255), 0);    
    speed=random(0.5,2);
  }
  
  void draw() {
    y=y+speed;
    if(y>height) y=0;
  
    float newVal=abs(bufFFT[bufIndex]*50)+2;
    val=val*(1-damper)+newVal*damper;
    
    fill(col);
    ellipse(x,y, val,val);
  }
}

