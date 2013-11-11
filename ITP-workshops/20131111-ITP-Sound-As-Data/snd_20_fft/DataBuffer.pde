class DataBuffer {
  float damper=-1;
  float max=0.5f,maxDamper=0.01f;
  float raw[],data[];
  int n;
  
  public DataBuffer(int n) {
    this.n=n;
    raw=new float[n];
    data=new float[n];
  }
  
  void setDamper(float valDamp,float maxDamp) {
    maxDamper=maxDamp;
    damper=valDamp;
  }
  
  float get(int index) {
    return data[index%n];
  }
  
  void update(float input[]) {
    float maxNow=0;
    for(float f:input) maxNow=maxNow>f ? maxNow : f;
    if(maxNow<0.001f) maxNow=0.001f;
    
    if(maxDamper>0) {
      max=max*(1-maxDamper)+maxNow*maxDamper;
    }
    else max=maxNow;
    
    System.arraycopy(input, 0, raw, 0, n);
    
    for(int i=0; i<n; i++) {
      float val=raw[i]/max;
      val=(val>1 ? 1 : val);

      if(damper>0) {
        data[i]=data[i]*(1-damper)+val*damper;
      }
      else data[i]=val;
    }
    
  }

  public void draw(PApplet p,float h) {
    
    // draw frame + ticks
    p.pushStyle();
    p.stroke(100);
    p.line(0, 0, n,0);
    p.line(0, h, n,h);
    p.noStroke();
    
    p.fill(100);
    for(int i=0; i<11; i++) {
      int id=(int)p.map(i, 0, 10, 0, n-1);
      p.rect(id, 0, 1, h);
    }
    p.popStyle();
    

    for(int i=0; i<n; i++) {
      float val=get(i)*h+1;
      p.rect(i,h-val, 1,val);
    }
  }
}
